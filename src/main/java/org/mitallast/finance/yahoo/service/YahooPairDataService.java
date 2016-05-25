package org.mitallast.finance.yahoo.service;

import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.processor.YahooPairDataProcessor;
import org.mitallast.finance.yahoo.repository.YahooQuoteRepository;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

@Service
public class YahooPairDataService {

    private final static Logger logger = LoggerFactory.getLogger(YahooPairDataService.class);

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooQuoteRepository quoteRepository;

    public final static int descriptive = 50;
    public final static int BPUsed = 10000;

    public Set<YahooPair> getPairs(YahooIndustry industry) {
        Iterable<YahooTicker> tickers = tickerRepository.findAllByIndustryAndBlacklist(industry);
        Set<YahooPair> pairs = new HashSet<>(100);
        List<YahooTicker> tickerList = new ArrayList<>(100);
        for (YahooTicker ticker : tickers) {
            tickerList.add(ticker);
        }
        for (int left = 0; left < tickerList.size(); left++) {
            for (int right = left + 1; right < tickerList.size(); right++) {
                pairs.add(new YahooPair(tickerList.get(left), tickerList.get(right)));
            }
        }
        return pairs;
    }

    public YahooPairDataProcessor getPairData(YahooPair pair) {
        logger.info("fetch pair data");
        YahooPairDataProcessor pairData = new YahooPairDataProcessor(
                pair,
                quoteRepository.findByTickerOrderByDateAsc(pair.getLeft()),
                quoteRepository.findByTickerOrderByDateAsc(pair.getRight())
        );
        logger.info("fetch pair data done");
        return pairData;
    }

    @Async
    public Future<YahooPair> updatePair(YahooPair pair) throws InterruptedException {
        try {
            update(pair);
            return new AsyncResult<>(pair);
        } catch (Throwable e) {
            logger.warn("error", e);
            throw e;
        }
    }

    public void update(YahooPair pair) {
        YahooPairDataProcessor pairData = getPairData(pair);
        if (pairData.getLeftArray().length > descriptive) {
            pair.setCorrelation(pairData.getCorrelation());
            pair.setSmaDeltaStdOutCount1(pairData.getSMADeltaStdOutCount(descriptive, 1));
            pair.setSmaDeltaStdOutCount2(pairData.getSMADeltaStdOutCount(descriptive, 2));
            pair.setSmaDeltaStdOutCount3(pairData.getSMADeltaStdOutCount(descriptive, 3));
            pair.setSmaDeltaBackTrade(pairData.getSMADeltaBackTrade(descriptive, BPUsed));
            pair.setPairTest(pairData.getPairTest());
            pair.setPairHiLow(pairData.getPairHiLow());
        } else {
            logger.warn("too small data size");
        }
    }
}

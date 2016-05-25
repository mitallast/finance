package org.mitallast.finance.yahoo.service;

import com.googlecode.jcsv.reader.CSVReader;
import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooQuoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Service
public class YahooQuoteFetchService extends AsyncService {

    private final static Logger logger = LoggerFactory.getLogger(YahooQuoteFetchService.class);

    @Autowired
    private YahooHttpService yahooService;

    @Autowired
    private YahooQuoteRepository quoteRepository;

    public Future<List<YahooQuote>> fetch(final YahooTicker ticker) {
        if (ticker.getId() == 0) {
            throw new RuntimeException("Ticker is not saved");
        }
        return pool.submit(new Callable<List<YahooQuote>>() {
            @Override
            public List<YahooQuote> call() throws Exception {
                logger.info("fetch ticker {}", ticker);

                List<YahooQuote> quotes = new ArrayList<>();
                CSVReader<YahooQuote> reader = yahooService.getQuote(ticker.getName());
                for (YahooQuote quote : reader) {
                    quotes.add(quote);
                    quote.setTicker(ticker);
                }
                reader.close();

                Iterable<YahooQuote> oldQuotes = quoteRepository.findByTicker(ticker);
                quoteRepository.delete(oldQuotes);
                quoteRepository.save(quotes);

                return quotes;
            }
        });
    }
}

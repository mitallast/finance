package org.mitallast.finance.processor;

import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.junit.Test;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.processor.YahooPairDataProcessor;
import org.mitallast.finance.yahoo.service.YahooPairDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class YahooPairDataProcessorTest {

    private final static Logger logger = LoggerFactory.getLogger(YahooPairDataProcessorTest.class);

    @Test
    public void testGetSMADeltaStdOutCount() {
        YahooTicker left = new YahooTicker();
        YahooTicker right = new YahooTicker();
        YahooPair pair = new YahooPair(left, right);

        ArrayList<YahooQuote> leftQuotes = quotes(new double[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
        ArrayList<YahooQuote> rightQuotes = quotes(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        YahooPairDataProcessor processor = new YahooPairDataProcessor(pair, leftQuotes, rightQuotes);
        logger.info("getSMADelta {}", getArray(processor.getSMADelta(YahooPairDataService.descriptive)));
        logger.info("getSMADeltaStd {}", processor.getSMADeltaSTD(YahooPairDataService.descriptive));
        int cnt = processor.getSMADeltaStdOutCount(YahooPairDataService.descriptive, 1);
        logger.info("getSMADeltaStdOutCount {}", cnt);
        assert cnt == 2;
    }

    @Test
    public void testGetSMADeltaStdOutCount2() {
        YahooTicker left = new YahooTicker();
        YahooTicker right = new YahooTicker();
        YahooPair pair = new YahooPair(left, right);

        ArrayList<YahooQuote> leftQuotes = quotes(new double[]{0, 0, 9, 5, 5, 2, 2, 8, 8, 1, 1, 6, 6, 4, 4, 2, 2, 8, 8, 9});
        ArrayList<YahooQuote> rightQuotes = quotes(new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        YahooPairDataProcessor processor = new YahooPairDataProcessor(pair, leftQuotes, rightQuotes);
        logger.info("getSMADelta {}", (getArray(processor.getSMADelta(YahooPairDataService.descriptive))));
        logger.info("getSMADeltaStd {}", processor.getSMADeltaSTD(YahooPairDataService.descriptive));
        int cnt = processor.getSMADeltaStdOutCount(YahooPairDataService.descriptive, 1);
        logger.info("getSMADeltaStdOutCount {}", cnt);
        assert cnt == 1;
    }

    private static ArrayList<YahooQuote> quotes(double[] quotes) {
        ArrayList<YahooQuote> quoteList = new ArrayList<>();
        for (int i = 0; i < quotes.length; i++) {
            double quote = quotes[i];
            quoteList.add(new YahooQuote(new Date(1000000 + 100000000 * i), quote));
        }
        return quoteList;
    }

    private static double[] getArray(TimeSeries series) {
        ArrayList<TimeSeriesDataItem> timeSeriesDataItems = new ArrayList<>(100);
        for (Object object : series.getItems()) {
            if (object instanceof TimeSeriesDataItem) {
                timeSeriesDataItems.add((TimeSeriesDataItem) object);
            }
        }
        Collections.sort(timeSeriesDataItems, new Comparator<TimeSeriesDataItem>() {
            @Override
            public int compare(TimeSeriesDataItem o1, TimeSeriesDataItem o2) {
                return (int) (o1.getPeriod().getStart().getTime() - o2.getPeriod().getStart().getTime());
            }
        });

        double[] list = new double[timeSeriesDataItems.size()];
        int i = 0;
        for (TimeSeriesDataItem dataItem : timeSeriesDataItems) {
            list[i++] = dataItem.getValue().doubleValue();
        }
        return list;
    }
}

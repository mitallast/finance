package org.mitallast.finance.yahoo.processor;

import gnu.trove.list.TDoubleList;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.map.hash.TObjectDoubleHashMap;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.inference.TestUtils;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("SuspiciousNameCombination")
public class YahooPairDataProcessor {

    private final static Logger logger = LoggerFactory.getLogger(YahooPairDataProcessor.class);

    private final YahooPair pair;
    private final double[] leftArray;
    private final double[] rightArray;
    private final Day[] dayArray;

    private final double[] leftDiffLogArray;
    private final double[] rightDiffLogArray;

    public YahooPairDataProcessor(YahooPair pair, Iterable<YahooQuote> leftQuotes, Iterable<YahooQuote> rightQuotes) {
        logger.debug("fetch left");
        TObjectDoubleMap<Date> left = new TObjectDoubleHashMap<>(2000);
        for (YahooQuote leftQuote : leftQuotes) {
            left.put(leftQuote.getDate(), leftQuote.getAdjClose());
        }
        logger.debug("fetch left done");
        logger.info("left size {}", left.size());

        TDoubleArrayList leftList = new TDoubleArrayList(2000);
        TDoubleArrayList rightList = new TDoubleArrayList(2000);
        ArrayList<Day> dayList = new ArrayList<>(2000);

        logger.debug("fetch right");
        double noValue = left.getNoEntryValue();
        int rightCount=0;
        for (YahooQuote rightQuote : rightQuotes) {
            rightCount++;
            double leftQuoteAdjClose = left.get(rightQuote.getDate());
            if (leftQuoteAdjClose != noValue) {
                Day day = new Day(rightQuote.getDate());
                leftList.add(leftQuoteAdjClose);
                rightList.add(rightQuote.getAdjClose());
                dayList.add(day);
            }
        }
        logger.debug("fetch right done");
        logger.info("right size {}", rightCount);

        logger.debug("fetch array");
        this.pair = pair;
        this.leftArray = leftList.toArray();
        this.rightArray = rightList.toArray();
        this.dayArray = dayList.toArray(new Day[dayList.size()]);
        logger.debug("fetch array done");

        logger.debug("fetch diff log array");
        int size = leftArray.length - 1;
        logger.info("size {}", size);
        if(size >= 1){
            leftDiffLogArray = new double[size];
            rightDiffLogArray = new double[size];
            for (int i = 0; i < size; i++) {
                leftDiffLogArray[i] = leftArray[i + 1] - leftArray[i];
                rightDiffLogArray[i] = rightArray[i + 1] - rightArray[i];
            }
            logger.debug("fetch diff log array done");
        }else {
            leftDiffLogArray = new double[0];
            rightDiffLogArray = new double[0];
        }
    }

    public double[] getLeftArray() {
        return leftArray;
    }

    public double[] getRightArray() {
        return rightArray;
    }

    public TimeSeries getLeftFeed() {
        TimeSeries feed = new TimeSeries(pair.getLeft().getName());
        for (int i = 0; i < leftArray.length; i++) {
            feed.addOrUpdate(dayArray[i], leftArray[i]);
        }
        return feed;
    }

    public TimeSeries getRightFeed() {
        TimeSeries feed = new TimeSeries(pair.getRight().getName());
        for (int i = 0; i < rightArray.length; i++) {
            feed.addOrUpdate(dayArray[i], rightArray[i]);
        }
        return feed;
    }

    public TimeSeries getPairFeed() {
        TimeSeries feed = new TimeSeries("PAIR " + pair.getTitle());
        for (int i = 0; i < rightArray.length; i++) {
            feed.addOrUpdate(dayArray[i], leftArray[i] / rightArray[i]);
        }
        return feed;
    }

    public double getPairHiLow() {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        double last = 0;
        for (int i = 0; i < rightArray.length; i++) {
            last = leftArray[i] / rightArray[i];
            if (last > max) {
                max = last;
            }
            if (last < min) {
                min = last;
            }
        }
        double range = (max - min) / 2;
        double lastMean = last - min - range;
        return Math.abs(lastMean / range);
    }

    public double getCorrelation() {
        return new PearsonsCorrelation().correlation(leftDiffLogArray, rightDiffLogArray);
    }

    public TimeSeries getSimpleMovingAverage(int windowSize) {
        TimeSeries smaFeed = new TimeSeries("SMA(" + windowSize + ") " + pair.getTitle());
        for (int day = windowSize; day < leftArray.length; day++) {
            double sum = 0.0;
            for (int i = day - windowSize; i < day; i++) {
                sum += leftArray[i] / rightArray[i];
            }
            smaFeed.addOrUpdate(dayArray[day], sum / windowSize);
        }
        return smaFeed;
    }

    public TimeSeries getSMADelta(int windowSize) {
        TimeSeries smaFeed = new TimeSeries("SMA-DELTA(" + windowSize + ") " + pair.getTitle());
        for (int day = windowSize; day < leftArray.length; day++) {
            double sum = 0.0;
            for (int i = day - windowSize; i < day; i++) {
                sum += leftArray[i] / rightArray[i];
            }
            double sma = sum / windowSize;
            double ratio = leftArray[day] / rightArray[day];
            smaFeed.addOrUpdate(dayArray[day], ratio - sma);
        }
        return smaFeed;
    }

    public double getSMADeltaSTD(int windowSize) {
        TDoubleList list = new TDoubleArrayList(leftArray.length - windowSize);
        for (int day = windowSize; day < leftArray.length; day++) {
            double sum = 0.0;
            for (int i = day - windowSize; i < day; i++) {
                sum += leftArray[i] / rightArray[i];
            }
            double sma = sum / windowSize;
            double ratio = leftArray[day] / rightArray[day];
            list.add(ratio - sma);
        }
        double[] smaFeed = list.toArray();
        StandardDeviation deviation = new StandardDeviation();
        return deviation.evaluate(smaFeed);
    }

    public int getSMADeltaStdOutCount(int windowSize, int level) {
        int counter = 0;
        double std = getSMADeltaSTD(windowSize) * level;
        boolean out = false;
        for (int day = windowSize; day < leftArray.length; day++) {
            double sum = 0.0;
            for (int i = day - windowSize; i < day; i++) {
                sum += leftArray[i] / rightArray[i];
            }
            double sma = sum / windowSize;
            double ratio = leftArray[day] / rightArray[day];
            double smaDelta = ratio - sma;
            if (Math.abs(smaDelta) > Math.abs(std)) {
                if (!out) {
                    counter++;
                }
                out = true;
            } else {
                out = false;
            }
        }
        return counter;
    }

    public double getSMADeltaBackTrade(int windowSize, double BPUsed) {
        double std = getSMADeltaSTD(windowSize);

        double leftStart = 0.0;
        double rightStart = 0.0;
        double totalSum = 0.0;

        boolean leftSell = false;

        boolean out = false;
        for (int day = windowSize; day < leftArray.length; day++) {
            double sum = 0.0;
            for (int i = day - windowSize; i < day; i++) {
                sum += leftArray[i] / rightArray[i];
            }
            double sma = sum / windowSize;
            double ratio = leftArray[day] / rightArray[day];
            double smaDelta = ratio - sma;
            if (Math.abs(smaDelta) * 2 > Math.abs(std)) {
                leftSell = std > 0;
                if (!out) {
                    leftStart = leftArray[day];
                    rightStart = rightArray[day];
                }
                out = true;
            } else if (out) {
                if (Math.abs(smaDelta) < Math.abs(std)) {
                    int leftCount = (int) Math.ceil((BPUsed / 2) / leftStart);
                    int rightCount = (int) Math.ceil((BPUsed / 2) / rightStart);
                    if (leftSell) {
                        // если значение больше 2сд первую акцию продать, вторую купить
                        totalSum += (leftArray[day] - leftStart) * leftCount;
                        totalSum += (rightStart - rightArray[day]) * rightCount;
                    } else {
                        // если значение больше -2сд первую акцию купить, вторую продать
                        totalSum += (leftStart - leftArray[day]) * leftCount;
                        totalSum += (rightArray[day] - rightStart) * rightCount;
                    }
                    out = false;
                }
            }
        }
        return totalSum;
    }

    public TimeSeries getExponentialMovingAverage(int windowSize) {
        TimeSeries emaFeed = new TimeSeries("EMA(" + windowSize + ") " + pair.getTitle());
        for (int day = windowSize; day < leftArray.length; day++) {
            double alpha = 0.5;
            double buffer = 0;
            for (int i = day - windowSize; i < day; i++) {
                double d = leftArray[i] / rightArray[i];
                buffer = buffer + alpha * (d - buffer);
            }
            emaFeed.addOrUpdate(dayArray[day], buffer);
        }
        return emaFeed;
    }

    public TimeSeries getMean(int windowSize) {
        DescriptiveStatistics statistics = new DescriptiveStatistics(windowSize);
        TimeSeries feed = new TimeSeries("MEAN(" + windowSize + ") " + pair.getTitle());
        for (int count = 0; count < leftArray.length; count++) {
            statistics.addValue(leftArray[count] / rightArray[count]);
            feed.addOrUpdate(dayArray[count], statistics.getMean());
        }
        return feed;
    }

    public TimeSeries getGeometricMean(int windowSize) {
        DescriptiveStatistics statistics = new DescriptiveStatistics(windowSize);
        TimeSeries feed = new TimeSeries("GMEAN(" + windowSize + ") " + pair.getTitle());
        for (int count = 0; count < leftArray.length; count++) {
            statistics.addValue(leftArray[count] / rightArray[count]);
            feed.addOrUpdate(dayArray[count], statistics.getGeometricMean());
        }
        return feed;
    }

    public TimeSeries getKurtosis(int windowSize) {
        DescriptiveStatistics statistics = new DescriptiveStatistics(windowSize);
        TimeSeries feed = new TimeSeries("KURTOSIS(" + windowSize + ")" + pair.getTitle());
        for (int count = 0; count < leftArray.length; count++) {
            statistics.addValue(leftArray[count] / rightArray[count]);
            feed.addOrUpdate(dayArray[count], statistics.getKurtosis());
        }
        return feed;
    }

    public TimeSeries getSkewness(int windowSize) {
        DescriptiveStatistics statistics = new DescriptiveStatistics(windowSize);
        TimeSeries feed = new TimeSeries("SKEWNESS(" + windowSize + ")" + pair.getTitle());
        for (int count = 0; count < leftArray.length; count++) {
            statistics.addValue(leftArray[count] / rightArray[count]);
            feed.addOrUpdate(dayArray[count], statistics.getSkewness());
        }
        return feed;
    }

    public TimeSeries getCorrelationFeed(int correlationSize) {
        double[] leftRangeArray = new double[correlationSize];
        double[] rightRangeArray = new double[correlationSize];

        TimeSeries correlationFeed = new TimeSeries("COR(" + correlationSize + ") " + pair.getTitle());
        for (int i = correlationSize; i < leftDiffLogArray.length; i++) {
            System.arraycopy(leftDiffLogArray, i - correlationSize, leftRangeArray, 0, correlationSize);
            System.arraycopy(rightDiffLogArray, i - correlationSize, rightRangeArray, 0, correlationSize);
            double correlation = new PearsonsCorrelation().correlation(leftRangeArray, rightRangeArray);
            // equals : diff log array [i] = array[i] - array[i-1]
            correlationFeed.addOrUpdate(dayArray[i + 1], correlation);
        }
        return correlationFeed;
    }

    public double getPairTest() {

//        d§ouble[][] vector = new double[leftArray.length][2];
//        for(int i=0; i<leftArray.length; i++){
//            vector[i][0] = leftArray[i];
//            vector[i][1] = rightArray[i];
//        }
//
//        MultivariateSimpleTimeSeries timeSeries = new MultivariateSimpleTimeSeries(vector);
//        CointegrationMLE coint = new CointegrationMLE(timeSeries, true, 2);
//        ImmutableVector eigenvalues = coint.getEigenvalues();
//        System.out.println("eigenvalues:");
//        System.out.println(eigenvalues);
//        System.out.println("cointegrating factors");
//        System.out.println(coint.beta());
//        System.out.println("speeds of adjustment");
//        System.out.println(coint.alpha());
//        System.out.println("test statistics");
//        JohansenTest test = new JohansenTest(
//        JohansenAsymptoticDistribution.Test.EIGEN,
//        JohansenAsymptoticDistribution.TrendType.CONSTANT,
//        eigenvalues.size());
//        System.out.println(test.getStats(coint));

        return TestUtils.pairedTTest(leftArray, rightArray);
    }
}

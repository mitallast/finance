package org.mitallast.finance.controller;

import org.mitallast.finance.chart.ChartBuilder;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.processor.YahooPairDataProcessor;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.mitallast.finance.yahoo.service.YahooPairDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/pair/chart")
public class PairChartController {

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooPairDataService pairService;

    @RequestMapping("double")
    public void chartDouble(@RequestParam(value = "left") Long leftId,
                            @RequestParam(value = "right") Long rightId,
                            @RequestParam(value = "width", defaultValue = "1200") int width,
                            @RequestParam(value = "height", defaultValue = "800") int height,
                            HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getLeftFeed());
        builder.addSeries(pairData.getRightFeed());
        builder.render(response);
    }

    @RequestMapping("/pair")
    public void chartPair(@RequestParam(value = "left") Long leftId,
                          @RequestParam(value = "right") Long rightId,
                          @RequestParam(value = "width", defaultValue = "1200") int width,
                          @RequestParam(value = "height", defaultValue = "800") int height,
                          HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getPairFeed());
        builder.render(response);
    }

    @RequestMapping("/correlation")
    public void chartPairCorrelation(@RequestParam(value = "left") Long leftId,
                                     @RequestParam(value = "right") Long rightId,
                                     @RequestParam(value = "width", defaultValue = "1200") int width,
                                     @RequestParam(value = "height", defaultValue = "800") int height,
                                     HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getCorrelationFeed(50));
        builder.addSeries(pairData.getCorrelationFeed(100));
        builder.addSeries(pairData.getCorrelationFeed(200));
        builder.render(response);
    }

    @RequestMapping("/moving")
    public void chartPairSMA(@RequestParam(value = "left") Long leftId,
                             @RequestParam(value = "right") Long rightId,
                             @RequestParam(value = "width", defaultValue = "1200") int width,
                             @RequestParam(value = "height", defaultValue = "800") int height,
                             HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getPairFeed());
        builder.addSeries(pairData.getSimpleMovingAverage(YahooPairDataService.descriptive));
        builder.addSeries(pairData.getExponentialMovingAverage(YahooPairDataService.descriptive));
        builder.render(response);
    }

    @RequestMapping("/moving/delta")
    public void chartPairSMADelta(@RequestParam(value = "left") Long leftId,
                                  @RequestParam(value = "right") Long rightId,
                                  @RequestParam(value = "width", defaultValue = "1200") int width,
                                  @RequestParam(value = "height", defaultValue = "800") int height,
                                  HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getSMADelta(YahooPairDataService.descriptive));
        builder.addStdMarker(pairData.getSMADeltaSTD(YahooPairDataService.descriptive));
        builder.render(response);
    }

    @RequestMapping("/mean")
    public void chartPairMean(@RequestParam(value = "left") Long leftId,
                              @RequestParam(value = "right") Long rightId,
                              @RequestParam(value = "width", defaultValue = "1200") int width,
                              @RequestParam(value = "height", defaultValue = "800") int height,
                              HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getPairFeed());
        builder.addSeries(pairData.getMean(YahooPairDataService.descriptive));
        builder.addSeries(pairData.getGeometricMean(YahooPairDataService.descriptive));
        builder.render(response);
    }

    @RequestMapping("/kurtosis")
    public void chartPairKurtosis(@RequestParam(value = "left") Long leftId,
                                  @RequestParam(value = "right") Long rightId,
                                  @RequestParam(value = "width", defaultValue = "1200") int width,
                                  @RequestParam(value = "height", defaultValue = "800") int height,
                                  HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getKurtosis(YahooPairDataService.descriptive));
        builder.render(response);
    }

    @RequestMapping("/skewness")
    public void chartPairSkewness(@RequestParam(value = "left") Long leftId,
                                  @RequestParam(value = "right") Long rightId,
                                  @RequestParam(value = "width", defaultValue = "1200") int width,
                                  @RequestParam(value = "height", defaultValue = "800") int height,
                                  HttpServletResponse response
    ) throws IOException {
        YahooTicker left = tickerRepository.findOne(leftId);
        YahooTicker right = tickerRepository.findOne(rightId);
        YahooPair pair = new YahooPair(left, right);
        YahooPairDataProcessor pairData = pairService.getPairData(pair);

        ChartBuilder builder = new ChartBuilder("Pair " + pair.getTitle(), width, height);
        builder.addSeries(pairData.getSkewness(YahooPairDataService.descriptive));
        builder.render(response);
    }
}

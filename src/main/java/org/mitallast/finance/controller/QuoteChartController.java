package org.mitallast.finance.controller;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.mitallast.finance.chart.ChartBuilder;
import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooQuoteRepository;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/quote")
public class QuoteChartController {

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooQuoteRepository quoteRepository;

    @RequestMapping("/chart")
    public void chart(YahooTicker ticker,
                      @RequestParam(value = "width", defaultValue = "1200") int width,
                      @RequestParam(value = "height", defaultValue = "800") int height,
                      HttpServletResponse response
    ) throws IOException {
        ticker = tickerRepository.findOne(ticker.getId());

        TimeSeries feed = new TimeSeries(ticker.getName());
        for (YahooQuote quote : quoteRepository.findByTicker(ticker)) {
            feed.addOrUpdate(new Day(quote.getDate()), quote.getAdjClose());
        }

        ChartBuilder builder = new ChartBuilder("Quotes", width, height);
        builder.addSeries(feed);
        builder.render(response);
    }
}

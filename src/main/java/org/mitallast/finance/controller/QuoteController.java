package org.mitallast.finance.controller;

import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooQuoteRepository;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.mitallast.finance.yahoo.service.YahooQuoteFetchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/quote")
public class QuoteController {

    private final static Logger logger = LoggerFactory.getLogger(QuoteController.class);

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooQuoteRepository quoteRepository;

    @Autowired
    private YahooQuoteFetchService quoteFetchService;

    @RequestMapping("fetch")
    @Transactional
    public String fetch(YahooTicker ticker, Model model) throws IOException, ExecutionException, InterruptedException {
        ticker = tickerRepository.findOne(ticker.getId());
        logger.info("fetch ticker {}", ticker);
        model.addAttribute("quotes", quoteFetchService.fetch(ticker).get());
        model.addAttribute("ticker", ticker);
        return "/quote/list";
    }

    @RequestMapping("fetch/all")
    public String fetchAll() {
        for (YahooTicker ticker : tickerRepository.findAllByBlacklist()) {
            quoteFetchService.fetch(ticker);
        }
        return "redirect:/quote/fetch/status";
    }

    @RequestMapping("fetch/status")
    public String status() {
        return "/quote/status";
    }

    @RequestMapping("list")
    public String list(YahooTicker ticker, Model model) {
        ticker = tickerRepository.findOne(ticker.getId());
        Iterable<YahooQuote> quotes = quoteRepository.findByTicker(ticker);
        model.addAttribute("quotes", quotes);
        model.addAttribute("ticker", ticker);
        return "/quote/list";
    }

    @ModelAttribute("queueSize")
    public int queueSize() {
        return quoteFetchService.size();
    }
}
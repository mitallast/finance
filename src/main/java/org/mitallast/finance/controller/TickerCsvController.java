package org.mitallast.finance.controller;

import com.googlecode.jcsv.reader.CSVReader;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.mitallast.finance.yahoo.service.YahooCsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/ticker/csv")
public class TickerCsvController {

    private final static Logger logger = LoggerFactory.getLogger(TickerCsvController.class);

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooCsvService csvService;

    @Autowired
    private Validator validator;

    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public String form() {
        return "/ticker/csv/upload";
    }

    @RequestMapping(value = "blacklist", method = RequestMethod.GET)
    public String blacklist() {
        return "/ticker/csv/blacklist";
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @Transactional
    public String handleTickerUpload(@RequestParam("csv") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try (CSVReader<YahooTicker> reader = csvService.getTickerReader(file.getInputStream())) {
                saveTickers(reader);
            }
            return "redirect:/ticker";
        } else {
            throw new IOException("Empty csv file");
        }
    }

    @RequestMapping(value = "upload/iris", method = RequestMethod.POST)
    @Transactional
    public String handleTickerIrisUpload(@RequestParam("csv") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try (CSVReader<YahooTicker> reader = csvService.getTickerIrisReader(file.getInputStream())) {
                saveTickers(reader);
            }
            return "redirect:/ticker";
        } else {
            throw new IOException("Empty csv file");
        }
    }

    @RequestMapping(value = "blacklist", method = RequestMethod.POST)
    @Transactional
    public String handleBlacklistUpload(@RequestParam("csv") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try (CSVReader<YahooTicker> reader = csvService.getTickerReader(file.getInputStream())) {
                blacklistTickers(reader);
            }
            return "redirect:/ticker";
        } else {
            throw new IOException("Empty csv file");
        }
    }

    @RequestMapping(value = "blacklist/iris", method = RequestMethod.POST)
    @Transactional
    public String handleBlacklistIrisUpload(@RequestParam("csv") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try (CSVReader<YahooTicker> reader = csvService.getTickerIrisReader(file.getInputStream())) {
                blacklistTickers(reader);
            }
            return "redirect:/ticker";
        } else {
            throw new IOException("Empty csv file");
        }
    }

    private void saveTickers(CSVReader<YahooTicker> reader) {
        tickerRepository.deleteAll();

        List<YahooTicker> tickers = new ArrayList<>(1000);
        for (YahooTicker ticker : reader) {
            Set<ConstraintViolation<YahooTicker>> violations = validator.validate(ticker);
            if (violations.isEmpty()) {
                tickers.add(ticker);
            } else {
                logger.info("ticker {}", ticker);
                logger.info("violations {}", violations);
            }
        }

        tickerRepository.save(tickers);
    }

    private void blacklistTickers(CSVReader<YahooTicker> reader) {
        List<YahooTicker> tickers = new ArrayList<>(1000);
        for (YahooTicker ticker : reader) {
            ticker = tickerRepository.findByName(ticker.getName());
            if (ticker != null) {
                logger.info("mark to blacklist {}", ticker);
                ticker.setBlacklist(true);
                tickers.add(ticker);
            }
        }

        tickerRepository.save(tickers);
    }
}

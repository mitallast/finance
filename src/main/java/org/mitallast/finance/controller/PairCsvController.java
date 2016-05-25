package org.mitallast.finance.controller;

import com.googlecode.jcsv.reader.CSVReader;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.repository.YahooPairRepository;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/pair/csv")
public class PairCsvController {

    private final static Logger logger = LoggerFactory.getLogger(PairCsvController.class);

    @Autowired
    private YahooPairRepository pairRepository;

    @Autowired
    private YahooCsvService csvService;


    @RequestMapping(value = "/upload/volatility", method = RequestMethod.GET)
    public String volatility() {
        return "/pair/csv/volatility";
    }

    @RequestMapping(value = "upload/volatility", method = RequestMethod.POST)
    @Transactional
    public String handleTickerUpload(@RequestParam("csv") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            try (CSVReader<YahooPair> reader = csvService.getPairIrisReader(file.getInputStream())) {
                updatePairs(reader);
            }
            return "redirect:/pair";
        } else {
            throw new IOException("Empty csv file");
        }
    }

    private void updatePairs(CSVReader<YahooPair> reader) {
        List<YahooPair> tickers = new ArrayList<>(1000);
        for (YahooPair pair : reader) {
            YahooPair savedPair = pairRepository.findOneByLeftAndRight(pair.getLeft(), pair.getRight());
            if (savedPair != null) {
                logger.info("update volatility {}", pair.getVolatility());
                savedPair.setVolatility(pair.getVolatility());
                tickers.add(savedPair);
            }
        }
        pairRepository.save(tickers);
    }
}

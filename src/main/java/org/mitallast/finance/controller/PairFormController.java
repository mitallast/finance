package org.mitallast.finance.controller;

import org.mitallast.finance.yahoo.editor.YahooTickerEditor;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooPairRepository;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.mitallast.finance.yahoo.service.YahooPairService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/pair")
public class PairFormController {

    private static final Logger logger = LoggerFactory.getLogger(PairFormController.class);

    @Autowired
    private YahooPairService pairService;

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooPairRepository pairRepository;

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(YahooTicker.class, new YahooTickerEditor(tickerRepository));
    }

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(@ModelAttribute("pair") YahooPair pair) {
        return "/pair/form";
    }

    @RequestMapping(value = "form", method = RequestMethod.POST)
    @Transactional
    public String form(@Valid @ModelAttribute("pair") YahooPair pair, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "/pair/form";
        }

        try {
            pairService.indexPair(pair).get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("error create pair", e);
        }

        return "redirect:/pair";
    }
}

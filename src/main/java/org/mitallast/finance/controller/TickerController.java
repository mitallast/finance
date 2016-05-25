package org.mitallast.finance.controller;

import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooIndustryRepository;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Controller
@RequestMapping("/ticker")
public class TickerController {

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooIndustryRepository industryRepository;

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("tickers", tickerRepository.findAllWithIndustry());
        return "/ticker/list";
    }

    @RequestMapping("industry")
    public String index(YahooIndustry industry, Model model) {
        industry = industryRepository.findOne(industry.getId());
        model.addAttribute("tickers", tickerRepository.findAllByIndustry(industry));
        model.addAttribute("industry", industry);
        return "/ticker/list";
    }

    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(@ModelAttribute("ticker") YahooTicker ticker) {
        return "/ticker/form";
    }

    @RequestMapping(value = "form", method = RequestMethod.POST)
    @Transactional
    public String form(@Valid @ModelAttribute("ticker") YahooTicker ticker, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/ticker/form";
        }
        YahooIndustry industry = industryRepository.findByName(ticker.getIndustry().getName());
        if (industry == null) {
            industryRepository.save(ticker.getIndustry());
        } else {
            ticker.setIndustry(industry);
        }
        tickerRepository.save(ticker);
        return "redirect:/ticker";
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @Transactional
    public String delete(@ModelAttribute("ticker") YahooTicker ticker) {
        tickerRepository.delete(ticker);
        return "redirect:/ticker";
    }
}

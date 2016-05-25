package org.mitallast.finance.controller;

import org.mitallast.finance.yahoo.repository.YahooIndustryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/industry")
public class IndustryController {

    @Autowired
    private YahooIndustryRepository industryRepository;

    @RequestMapping("/")
    public String list(Model model) {
        model.addAttribute("industries", industryRepository.findAll());
        return "/industry/list";
    }
}

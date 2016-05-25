package org.mitallast.finance.controller;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.writer.CSVWriter;
import com.googlecode.jcsv.writer.internal.CSVWriterBuilder;
import org.mitallast.finance.yahoo.component.csv.ColumnJoiner;
import org.mitallast.finance.yahoo.component.csv.PairConverter;
import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.form.PairSearchForm;
import org.mitallast.finance.yahoo.form.specification.FormRange;
import org.mitallast.finance.yahoo.repository.YahooIndustryRepository;
import org.mitallast.finance.yahoo.repository.YahooPairRepository;
import org.mitallast.finance.yahoo.service.YahooPairService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/pair")
public class PairController {

    @Autowired
    private YahooIndustryRepository industryRepository;

    @Autowired
    private YahooPairService pairService;

    @Autowired
    private YahooPairRepository pairRepository;

    @RequestMapping("/")
    public String pairs(PairSearchForm form, Pageable pageable, Model model) {
        new FormRange<>(form.getRange(), pairRepository).load();
        Page<YahooPair> page = pairRepository.findAll(form.getSpecification(), pageable);
        model.addAttribute("form", form);
        model.addAttribute("page", page);
        model.addAttribute("pairs", page.getContent());
        model.addAttribute("industries", industryRepository.findAll());
        return "/pair/search";
    }

    @RequestMapping("/charts")
    public String charts(YahooPair pair, Model model) {
        model.addAttribute("pair", pairRepository.findOne(pair.getId()));
        return "/pair/charts";
    }

    @RequestMapping("/search/csv")
    public void pairsCsv(PairSearchForm form, HttpServletResponse response) throws IOException {
        List<YahooPair> pages = pairRepository.findAll(form.getSpecification());
        writeCsv(response, pages);
    }

    @RequestMapping("/industry")
    public String pairs(YahooIndustry industry, Model model) {
        industry = industryRepository.findOne(industry.getId());
        model.addAttribute("pairs", pairRepository.findAllByIndustry(industry));
        model.addAttribute("industry", industry);
        return "/pair/list";
    }

    @RequestMapping("/industry/index")
    public String indexPair(YahooIndustry industry) throws ExecutionException, InterruptedException {
        industry = industryRepository.findOne(industry.getId());
        pairService.indexPair(industry).get();
        return "redirect:/industry";
    }

    @RequestMapping("/index/all")
    public String indexPair() throws ExecutionException, InterruptedException {
        pairService.indexPair();
        return "redirect:/pair/index/status";
    }

    @RequestMapping("/index/industry/all")
    public String indexIndustryPair() throws ExecutionException, InterruptedException {
        for (YahooIndustry industry : industryRepository.findAll()) {
            pairService.indexPair(industry);
        }
        return "redirect:/pair/index/status";
    }

    @RequestMapping("/index/status")
    public String indexStatus() {
        return "/pair/status";
    }

    @RequestMapping("industry/csv")
    public void pairCsv(YahooIndustry industry, HttpServletResponse response) throws IOException {
        industry = industryRepository.findOne(industry.getId());
        Iterable<YahooPair> pairs = pairRepository.findAllByIndustry(industry);
        writeCsv(response, pairs);
    }

    @RequestMapping("csv/all")
    public void pairCsv(HttpServletResponse response) throws IOException {
        Iterable<YahooPair> pairs = pairRepository.findAllWithTicker();
        writeCsv(response, pairs);
    }

    @ModelAttribute("queueSize")
    public int queueSize() {
        return pairService.size();
    }

    private void writeCsv(HttpServletResponse response, Iterable<YahooPair> pairs) throws IOException {
        CSVWriter<YahooPair> csvWriter = new CSVWriterBuilder<YahooPair>(response.getWriter())
                .strategy(CSVStrategy.UK_DEFAULT)
                .columnJoiner(ColumnJoiner.instance)
                .entryConverter(PairConverter.instance)
                .build();

        for (YahooPair pair : pairs) {
            csvWriter.write(pair);
        }
        csvWriter.close();
    }
}

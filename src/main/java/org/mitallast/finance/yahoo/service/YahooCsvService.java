package org.mitallast.finance.yahoo.service;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.ValueProcessor;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.annotations.processors.DateProcessor;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooIndustryRepository;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class YahooCsvService {

    private final static int irisPair = 0;
    private final static int irisRSI = 1;
    private final static int irisVolatility = 2;
    private final static int irisPrevCloseSTD = 3;
    private final static int irisSTD = 4;
    private final static int irisIndustry = 5;

    @Autowired
    private YahooTickerRepository tickerRepository;

    @Autowired
    private YahooIndustryRepository industryRepository;

    public CSVReader<YahooTicker> getTickerIrisReader(InputStream stream) {
        CSVEntryParser<YahooTicker> entryParser = new CSVEntryParser<YahooTicker>() {
            private ValueProcessorProvider valueProvider = valueProvider();

            @Override
            public YahooTicker parseEntry(String... data) {
                YahooTicker ticker = new YahooTicker();
                if (data.length > irisPair) {
                    ticker.setName(parsePair(data[irisPair]));
                    if (data.length > irisIndustry) {
                        ticker.setIndustry(valueProvider.getValueProcessor(YahooIndustry.class).processValue(data[irisIndustry]));
                    }
                }
                return ticker;
            }

            public String parsePair(String pair) {
                int s = pair.indexOf(';');
                if (s > 0) {
                    return pair.substring(0, s);
                } else {
                    return pair;
                }
            }
        };

        return new CSVReaderBuilder<YahooTicker>(new InputStreamReader(stream))
                .entryParser(entryParser)
                .strategy(new CSVStrategy(',', '"', '#', true, true))
                .build();
    }

    public CSVReader<YahooTicker> getTickerReader(InputStream stream) {
        CSVEntryParser<YahooTicker> entryParser = new AnnotationEntryParser<>(YahooTicker.class, valueProvider());

        return new CSVReaderBuilder<YahooTicker>(new InputStreamReader(stream))
                .entryParser(entryParser)
                .strategy(new CSVStrategy('\t', '"', '#', false, true))
                .build();
    }

    public CSVReader<YahooPair> getPairIrisReader(InputStream stream) {
        CSVEntryParser<YahooPair> entryParser = new CSVEntryParser<YahooPair>() {
            private ValueProcessorProvider valueProvider = valueProvider();

            @Override
            public YahooPair parseEntry(String... data) {
                YahooPair pair = new YahooPair();
                if (data.length > irisPair) {
                    String pairName = data[irisPair];
                    int s = data[0].indexOf(';');
                    if (s > 0) {
                        String left = pairName.substring(0, s);
                        String right = pairName.substring(s + 2);
                        YahooTicker leftTicker = valueProvider.getValueProcessor(YahooTicker.class).processValue(left);
                        YahooTicker rightTicker = valueProvider.getValueProcessor(YahooTicker.class).processValue(right);
                        if (leftTicker != null && rightTicker != null) {
                            pair.setTickers(leftTicker, rightTicker);
                        }
                        if (data.length > irisVolatility) {
                            pair.setVolatility(Double.valueOf(data[irisVolatility]));
                        }
                    }
                }
                return pair;
            }
        };

        return new CSVReaderBuilder<YahooPair>(new InputStreamReader(stream))
                .entryParser(entryParser)
                .strategy(new CSVStrategy(',', '"', '#', true, true))
                .build();
    }

    public ValueProcessorProvider valueProvider() {
        ValueProcessorProvider valueProvider = new ValueProcessorProvider();
        valueProvider.removeValueProcessor(Date.class);
        valueProvider.registerValueProcessor(Date.class, new DateProcessor(new SimpleDateFormat("yyyy-MM-dd")));
        valueProvider.registerValueProcessor(YahooIndustry.class, new ValueProcessor<YahooIndustry>() {
            private Map<String, YahooIndustry> cache = new HashMap<>();

            @Override
            public YahooIndustry processValue(String name) {
                if (name.isEmpty()) {
                    return null;
                }
                YahooIndustry industry = cache.get(name);
                if (industry == null) {
                    industry = industryRepository.findByName(name);
                    if (industry == null) {
                        industry = new YahooIndustry();
                        industry.setName(name);
                        industryRepository.save(industry);
                    }
                    cache.put(name, industry);
                }
                return industry;
            }
        });
        valueProvider.registerValueProcessor(YahooTicker.class, new ValueProcessor<YahooTicker>() {
            private Map<String, YahooTicker> cache = new HashMap<>();

            @Override
            public YahooTicker processValue(String name) {
                if (name.isEmpty()) {
                    return null;
                }
                YahooTicker ticker = cache.get(name);
                if (ticker == null) {
                    ticker = tickerRepository.findByName(name);
                    cache.put(name, ticker);
                }
                return ticker;
            }
        });
        return valueProvider;
    }
}

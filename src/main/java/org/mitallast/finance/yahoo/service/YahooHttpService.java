package org.mitallast.finance.yahoo.service;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.annotations.processors.DateProcessor;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @link https://code.google.com/p/yahoo-finance-managed/wiki/csvHistQuotesDownload
 */
@Repository
public class YahooHttpService {
    private final static Logger logger = LoggerFactory.getLogger(YahooHttpService.class);
    private CloseableHttpClient httpClient;

    public YahooHttpService() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(1000, TimeUnit.MILLISECONDS);
        httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .build();
    }

    public CloseableHttpResponse request(String url) throws IOException {
        logger.info("prepare request {}", url);
        HttpGet request = new HttpGet(url);
        request.setConfig(RequestConfig.custom()
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .setSocketTimeout(10000)
                .build());

        int retry = 50;
        while (true){
            try{
                logger.info("send request {}", request);
                CloseableHttpResponse response = httpClient.execute(request);
                logger.info("response {}", response);
                int statusCode = response.getStatusLine().getStatusCode();
                if(statusCode != 200){
                    response.close();
                    throw new RuntimeException("Status code "+statusCode);
                }
                return response;
            }catch (RuntimeException e){
                if(--retry > 0){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interrupted) {
                        logger.error("interrupted", interrupted);
                    }
                }else {
                    throw e;
                }
            }
        }
    }

    public CSVReader<YahooQuote> getQuote(String ticker) throws IOException {
        Calendar end = Calendar.getInstance();
        end.setTime(new Date());

        Calendar start = Calendar.getInstance();
        start.setTime(new Date());
        start.add(Calendar.YEAR, -2);

        return getQuote(ticker, start, end);
    }

    public CSVReader<YahooQuote> getQuote(String ticker, Calendar start, Calendar end) throws IOException {
        logger.info("fetch quote from {} to {}", start, end);
        CloseableHttpResponse response = request("http://chart.yahoo.com/table.csv?s=" + ticker +
                "&a=" + start.get(Calendar.MONTH) + "&b=" + formatDate(start.get(Calendar.DAY_OF_MONTH)) + "&c=" + start.get(Calendar.YEAR) +
                "&d=" + end.get(Calendar.MONTH) + "&e=" + formatDate(end.get(Calendar.DAY_OF_MONTH)) + "&f=" + end.get(Calendar.YEAR) +
                "&g=d&q=q&y=0&z=" + ticker + "&x=.csv");
        HttpEntity httpEntity = response.getEntity();
        InputStream inputStream = httpEntity.getContent();

        logger.info("csv reader prepare");
        ValueProcessorProvider valueProvider = new ValueProcessorProvider();
        valueProvider.removeValueProcessor(Date.class);
        valueProvider.registerValueProcessor(Date.class, new DateProcessor(new SimpleDateFormat("yyyy-MM-dd")));
        CSVEntryParser<YahooQuote> entryParser = new AnnotationEntryParser<>(YahooQuote.class, valueProvider);

        CSVReader<YahooQuote> reader = new CSVReaderBuilder<YahooQuote>(new InputStreamReader(inputStream))
                .entryParser(entryParser)
                .strategy(new CSVStrategy(',', '"', '#', true, true))
                .build();
        logger.info("csv reader prepare done");

        return reader;
    }

    private String formatDate(int date) {
        if (date < 10) {
            return "0" + date;
        } else {
            return String.valueOf(date);
        }
    }
}
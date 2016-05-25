package org.mitallast.finance.yahoo.component.csv;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.util.CSVUtil;
import com.googlecode.jcsv.writer.CSVColumnJoiner;

import java.util.regex.Pattern;

public class ColumnJoiner implements CSVColumnJoiner {

    public static final CSVColumnJoiner instance = new ColumnJoiner();

    private static final Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?");

    @Override
    public String joinColumns(String[] data, CSVStrategy strategy) {
        final String delimiter = String.valueOf(strategy.getDelimiter());
        final String quote = String.valueOf(strategy.getQuoteCharacter());
        final String doubleQuote = quote + quote;
        for (int i = 0; i < data.length; i++) {
            if (numberPattern.matcher(data[i]).matches()) {
                data[i] = data[i];
            } else {
                if (data[i].contains(quote)) {
                    data[i] = data[i].replaceAll(Pattern.quote(quote), doubleQuote);
                }
                data[i] = quote + data[i] + quote;
            }
        }
        return CSVUtil.implode(data, delimiter);
    }
}

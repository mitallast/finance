package org.mitallast.finance.yahoo.component.csv;

import com.googlecode.jcsv.writer.CSVEntryConverter;
import org.mitallast.finance.yahoo.entity.YahooPair;

public class PairConverter implements CSVEntryConverter<YahooPair> {

    public final static CSVEntryConverter<YahooPair> instance = new PairConverter();

    @Override
    public String[] convertEntry(YahooPair pair) {
        return new String[]{
                pair.getLeft().getName(),
                pair.getLeft().getIndustry().getName(),
                pair.getRight().getName(),
                pair.getRight().getIndustry().getName(),
                String.valueOf(pair.getCorrelation()),
                String.valueOf(pair.getVolatility())
        };
    }
}

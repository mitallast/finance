package org.mitallast.finance.yahoo.editor;

import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.mitallast.finance.yahoo.repository.YahooTickerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyEditorSupport;

public class YahooTickerEditor extends PropertyEditorSupport {
    private final static Logger logger = LoggerFactory.getLogger(YahooTickerEditor.class);

    private final YahooTickerRepository tickerRepository;

    public YahooTickerEditor(YahooTickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }

    @Override
    public String getAsText() {
        return (getValue() != null)
                ? ((YahooTicker) getValue()).getName()
                : null;
    }

    @Override
    public void setAsText(String name) throws IllegalArgumentException {
        logger.info("set as text {}", name);
        YahooTicker ticker = tickerRepository.findByName(name);
        setValue(ticker);
    }
}

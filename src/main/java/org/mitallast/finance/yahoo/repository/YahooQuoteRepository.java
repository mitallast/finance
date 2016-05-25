package org.mitallast.finance.yahoo.repository;

import org.mitallast.finance.yahoo.entity.YahooQuote;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YahooQuoteRepository extends CrudRepository<YahooQuote, Long> {

    public Iterable<YahooQuote> findByTicker(YahooTicker ticker);

    @Query("SELECT new YahooQuote(date, adjClose) FROM YahooQuote where ticker = ?1 order by date asc")
    public Iterable<YahooQuote> findByTickerOrderByDateAsc(YahooTicker ticker);
}

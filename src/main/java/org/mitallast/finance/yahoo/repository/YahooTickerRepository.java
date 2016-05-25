package org.mitallast.finance.yahoo.repository;

import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YahooTickerRepository extends CrudRepository<YahooTicker, Long> {

    @Query("select t from YahooTicker t join fetch t.industry")
    Iterable<YahooTicker> findAllWithIndustry();

    @Query("select t from YahooTicker t join fetch t.industry where t.name = ?1")
    YahooTicker findByName(String name);

    @Query("select t from YahooTicker t join fetch t.industry where t.blacklist = false")
    Iterable<YahooTicker> findAllByBlacklist();

    @Query("select t from YahooTicker t join fetch t.industry where t.industry = ?1")
    Iterable<YahooTicker> findAllByIndustry(YahooIndustry industry);

    @Query("select t from YahooTicker t join fetch t.industry where t.blacklist = false and t.industry = ?1")
    Iterable<YahooTicker> findAllByIndustryAndBlacklist(YahooIndustry industry);
}

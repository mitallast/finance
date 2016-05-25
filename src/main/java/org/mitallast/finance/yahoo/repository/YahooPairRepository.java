package org.mitallast.finance.yahoo.repository;

import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.entity.YahooTicker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YahooPairRepository extends JpaRepository<YahooPair, Long>, JpaSpecificationExecutor<YahooPair> {

    @Query("select p from YahooPair p join fetch p.left join fetch p.right join fetch p.leftIndustry join fetch p.rightIndustry")
    List<YahooPair> findAllWithTicker();

    @Query("select p from YahooPair p join fetch p.left join fetch p.right join fetch p.leftIndustry join fetch p.rightIndustry where p.leftIndustry = ?1 or p.rightIndustry = ?1")
    List<YahooPair> findAllByIndustry(YahooIndustry industry);

    YahooPair findOneByLeftAndRight(YahooTicker left, YahooTicker right);
}

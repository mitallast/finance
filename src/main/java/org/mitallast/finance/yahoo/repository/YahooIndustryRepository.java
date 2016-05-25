package org.mitallast.finance.yahoo.repository;

import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YahooIndustryRepository extends CrudRepository<YahooIndustry, Long> {

    YahooIndustry findByName(String name);
}

package org.mitallast.finance.yahoo.form;

import org.mitallast.finance.yahoo.entity.YahooIndustry;
import org.mitallast.finance.yahoo.entity.YahooPair;
import org.mitallast.finance.yahoo.form.specification.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PairSearchForm {
    @Greater(field = "correlation")
    private Double correlationFrom;
    @Less(field = "correlation")
    private Double correlationTo;

    @Greater(field = "volatility")
    private Double volatilityFrom;
    @Less(field = "volatility")
    private Double volatilityTo;

    @Greater(field = "pairTest")
    private Double pairTestFrom;
    @Greater(field = "pairTest")
    private Double pairTestTo;

    @Greater(field = "pairHiLow")
    private Double pairHiLowFrom;
    @Greater(field = "pairHiLow")
    private Double pairHiLowTo;

    @Equals(field = {"leftIndustry", "rightIndustry"})
    private YahooIndustry industry;

    @Greater(field = "smaDeltaStdOutCount1")
    private Integer smaDeltaStdOutCount1From;
    @Less(field = "smaDeltaStdOutCount1")
    private Integer smaDeltaStdOutCount1To;
    @Greater(field = "smaDeltaStdOutCount2")
    private Integer smaDeltaStdOutCount2From;
    @Less(field = "smaDeltaStdOutCount2")
    private Integer smaDeltaStdOutCount2To;
    @Greater(field = "smaDeltaStdOutCount3")
    private Integer smaDeltaStdOutCount3From;
    @Less(field = "smaDeltaStdOutCount3")
    private Integer smaDeltaStdOutCount3To;

    @Greater(field = "smaDeltaBackTrade")
    private Double smaDeltaBackTradeFrom;
    @Less(field = "smaDeltaBackTrade")
    private Double smaDeltaBackTradeTo;

    @SortField
    private String sorting;
    @SortDirection
    private boolean desc = false;

    private final PairSearchRange range = new PairSearchRange();

    public PairSearchRange getRange() {
        return range;
    }

    public Double getCorrelationFrom() {
        return correlationFrom;
    }

    public void setCorrelationFrom(Double correlationFrom) {
        this.correlationFrom = correlationFrom;
    }

    public Double getCorrelationTo() {
        return correlationTo;
    }

    public void setCorrelationTo(Double correlationTo) {
        this.correlationTo = correlationTo;
    }

    public Double getVolatilityFrom() {
        return volatilityFrom;
    }

    public void setVolatilityFrom(Double volatilityFrom) {
        this.volatilityFrom = volatilityFrom;
    }

    public Double getVolatilityTo() {
        return volatilityTo;
    }

    public void setVolatilityTo(Double volatilityTo) {
        this.volatilityTo = volatilityTo;
    }

    public YahooIndustry getIndustry() {
        return industry;
    }

    public void setIndustry(YahooIndustry industry) {
        this.industry = industry;
    }

    public Integer getSmaDeltaStdOutCount1From() {
        return smaDeltaStdOutCount1From;
    }

    public void setSmaDeltaStdOutCount1From(Integer smaDeltaStdOutCount1From) {
        this.smaDeltaStdOutCount1From = smaDeltaStdOutCount1From;
    }

    public Integer getSmaDeltaStdOutCount1To() {
        return smaDeltaStdOutCount1To;
    }

    public void setSmaDeltaStdOutCount1To(Integer smaDeltaStdOutCount1To) {
        this.smaDeltaStdOutCount1To = smaDeltaStdOutCount1To;
    }

    public Integer getSmaDeltaStdOutCount2From() {
        return smaDeltaStdOutCount2From;
    }

    public void setSmaDeltaStdOutCount2From(Integer smaDeltaStdOutCount2From) {
        this.smaDeltaStdOutCount2From = smaDeltaStdOutCount2From;
    }

    public Integer getSmaDeltaStdOutCount2To() {
        return smaDeltaStdOutCount2To;
    }

    public void setSmaDeltaStdOutCount2To(Integer smaDeltaStdOutCount2To) {
        this.smaDeltaStdOutCount2To = smaDeltaStdOutCount2To;
    }

    public Integer getSmaDeltaStdOutCount3From() {
        return smaDeltaStdOutCount3From;
    }

    public void setSmaDeltaStdOutCount3From(Integer smaDeltaStdOutCount3From) {
        this.smaDeltaStdOutCount3From = smaDeltaStdOutCount3From;
    }

    public Integer getSmaDeltaStdOutCount3To() {
        return smaDeltaStdOutCount3To;
    }

    public void setSmaDeltaStdOutCount3To(Integer smaDeltaStdOutCount3To) {
        this.smaDeltaStdOutCount3To = smaDeltaStdOutCount3To;
    }

    public Double getSmaDeltaBackTradeFrom() {
        return smaDeltaBackTradeFrom;
    }

    public void setSmaDeltaBackTradeFrom(Double smaDeltaBackTradeFrom) {
        this.smaDeltaBackTradeFrom = smaDeltaBackTradeFrom;
    }

    public Double getSmaDeltaBackTradeTo() {
        return smaDeltaBackTradeTo;
    }

    public void setSmaDeltaBackTradeTo(Double smaDeltaBackTradeTo) {
        this.smaDeltaBackTradeTo = smaDeltaBackTradeTo;
    }

    public String getSorting() {
        return sorting;
    }

    public void setSorting(String sorting) {
        this.sorting = sorting;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }

    public Double getPairTestFrom() {
        return pairTestFrom;
    }

    public void setPairTestFrom(Double pairTestFrom) {
        this.pairTestFrom = pairTestFrom;
    }

    public Double getPairTestTo() {
        return pairTestTo;
    }

    public void setPairTestTo(Double pairTestTo) {
        this.pairTestTo = pairTestTo;
    }

    public Double getPairHiLowFrom() {
        return pairHiLowFrom;
    }

    public void setPairHiLowFrom(Double pairHiLowFrom) {
        this.pairHiLowFrom = pairHiLowFrom;
    }

    public Double getPairHiLowTo() {
        return pairHiLowTo;
    }

    public void setPairHiLowTo(Double pairHiLowTo) {
        this.pairHiLowTo = pairHiLowTo;
    }

    public Specification<YahooPair> getSpecification() {
        return new FormSpecification<>(this);
    }

    public String getParams(String sort, boolean desc) {
        List<String> params = getParamsList();
        params.add("sorting=" + sort);
        if (desc) {
            params.add("desc=true");
        }
        return renderUri(params);
    }

    public String getParams() {
        List<String> params = getParamsList();
        if (sorting != null) {
            params.add("sorting=" + sorting);
            if (desc) {
                params.add("desc=true");
            }
        }
        return renderUri(params);
    }

    private String renderUri(List<String> params) {
        String uri = "";
        for (int i = 0; i < params.size(); i++) {
            if (i > 0) {
                uri += '&';
            }
            uri += params.get(i);
        }
        if (!uri.isEmpty()) {
            uri = '?' + uri;
        }
        return uri;
    }

    private List<String> getParamsList() {
        List<String> params = new ArrayList<>(10);
        if (correlationFrom != null) {
            params.add("correlationFrom=" + correlationFrom);
        }
        if (correlationTo != null) {
            params.add("correlationTo=" + correlationTo);
        }
        if (volatilityFrom != null) {
            params.add("volatilityFrom=" + volatilityFrom);
        }
        if (volatilityTo != null) {
            params.add("volatilityTo=" + volatilityTo);
        }

        if (pairTestFrom != null) {
            params.add("pairTestFrom=" + pairTestFrom);
        }
        if (pairTestTo != null) {
            params.add("pairTestTo=" + pairTestTo);
        }
        if (pairHiLowFrom != null) {
            params.add("pairHiLowFrom=" + pairHiLowFrom);
        }
        if (pairHiLowTo != null) {
            params.add("pairHiLowTo=" + pairHiLowTo);
        }

        if (industry != null) {
            params.add("industry=" + industry.getId());
        }
        if (smaDeltaStdOutCount1From != null) {
            params.add("smaDeltaStdOutCount1From=" + smaDeltaStdOutCount1From);
        }
        if (smaDeltaStdOutCount1To != null) {
            params.add("smaDeltaStdOutCount1To=" + smaDeltaStdOutCount1To);
        }
        if (smaDeltaStdOutCount2From != null) {
            params.add("smaDeltaStdOutCount2From=" + smaDeltaStdOutCount2From);
        }
        if (smaDeltaStdOutCount2To != null) {
            params.add("smaDeltaStdOutCount2To=" + smaDeltaStdOutCount2To);
        }
        if (smaDeltaStdOutCount3From != null) {
            params.add("smaDeltaStdOutCount3From=" + smaDeltaStdOutCount3From);
        }
        if (smaDeltaStdOutCount3To != null) {
            params.add("smaDeltaStdOutCount3To=" + smaDeltaStdOutCount3To);
        }

        if (smaDeltaBackTradeFrom != null) {
            params.add("smaDeltaBackTradeFrom=" + smaDeltaBackTradeFrom);
        }
        if (smaDeltaBackTradeTo != null) {
            params.add("smaDeltaBackTradeTo=" + smaDeltaBackTradeTo);
        }
        return params;
    }
}

package org.mitallast.finance.yahoo.form;

import org.mitallast.finance.yahoo.form.specification.Max;
import org.mitallast.finance.yahoo.form.specification.Min;

@SuppressWarnings("unused")
public class PairSearchRange {
    @Min(field = "correlation")
    private Double correlationMin;
    @Max(field = "correlation")
    private Double correlationMax;

    @Min(field = "volatility")
    private Double volatilityMin;
    @Max(field = "volatility")
    private Double volatilityMax;

    @Min(field = "pairTest")
    private Double pairTestMin;
    @Max(field = "pairTest")
    private Double pairTestMax;

    @Min(field = "pairHiLow")
    private Double pairHiLowMin;
    @Max(field = "pairHiLow")
    private Double pairHiLowMax;

    @Min(field = "smaDeltaStdOutCount1")
    private Integer smaDeltaStdOutCount1Min;
    @Max(field = "smaDeltaStdOutCount1")
    private Integer smaDeltaStdOutCount1Max;
    @Min(field = "smaDeltaStdOutCount2")
    private Integer smaDeltaStdOutCount2Min;
    @Max(field = "smaDeltaStdOutCount2")
    private Integer smaDeltaStdOutCount2Max;
    @Min(field = "smaDeltaStdOutCount3")
    private Integer smaDeltaStdOutCount3Min;
    @Max(field = "smaDeltaStdOutCount3")
    private Integer smaDeltaStdOutCount3Max;

    @Min(field = "smaDeltaBackTrade")
    private Double smaDeltaBackTradeMin;
    @Max(field = "smaDeltaBackTrade")
    private Double smaDeltaBackTradeMax;

    public Double getCorrelationMin() {
        return correlationMin;
    }

    public void setCorrelationMin(Double correlationMin) {
        this.correlationMin = correlationMin;
    }

    public Double getCorrelationMax() {
        return correlationMax;
    }

    public void setCorrelationMax(Double correlationMax) {
        this.correlationMax = correlationMax;
    }

    public Double getVolatilityMin() {
        return volatilityMin;
    }

    public void setVolatilityMin(Double volatilityMin) {
        this.volatilityMin = volatilityMin;
    }

    public Double getVolatilityMax() {
        return volatilityMax;
    }

    public void setVolatilityMax(Double volatilityMax) {
        this.volatilityMax = volatilityMax;
    }

    public Double getPairTestMin() {
        return pairTestMin;
    }

    public void setPairTestMin(Double pairTestMin) {
        this.pairTestMin = pairTestMin;
    }

    public Double getPairTestMax() {
        return pairTestMax;
    }

    public Double getPairHiLowMin() {
        return pairHiLowMin;
    }

    public void setPairHiLowMin(Double pairHiLowMin) {
        this.pairHiLowMin = pairHiLowMin;
    }

    public Double getPairHiLowMax() {
        return pairHiLowMax;
    }

    public void setPairHiLowMax(Double pairHiLowMax) {
        this.pairHiLowMax = pairHiLowMax;
    }

    public void setPairTestMax(Double pairTestMax) {
        this.pairTestMax = pairTestMax;
    }

    public Integer getSmaDeltaStdOutCount1Min() {
        return smaDeltaStdOutCount1Min;
    }

    public void setSmaDeltaStdOutCount1Min(Integer smaDeltaStdOutCount1Min) {
        this.smaDeltaStdOutCount1Min = smaDeltaStdOutCount1Min;
    }

    public Integer getSmaDeltaStdOutCount1Max() {
        return smaDeltaStdOutCount1Max;
    }

    public void setSmaDeltaStdOutCount1Max(Integer smaDeltaStdOutCount1Max) {
        this.smaDeltaStdOutCount1Max = smaDeltaStdOutCount1Max;
    }

    public Integer getSmaDeltaStdOutCount2Min() {
        return smaDeltaStdOutCount2Min;
    }

    public void setSmaDeltaStdOutCount2Min(Integer smaDeltaStdOutCount2Min) {
        this.smaDeltaStdOutCount2Min = smaDeltaStdOutCount2Min;
    }

    public Integer getSmaDeltaStdOutCount2Max() {
        return smaDeltaStdOutCount2Max;
    }

    public void setSmaDeltaStdOutCount2Max(Integer smaDeltaStdOutCount2Max) {
        this.smaDeltaStdOutCount2Max = smaDeltaStdOutCount2Max;
    }

    public Integer getSmaDeltaStdOutCount3Min() {
        return smaDeltaStdOutCount3Min;
    }

    public void setSmaDeltaStdOutCount3Min(Integer smaDeltaStdOutCount3Min) {
        this.smaDeltaStdOutCount3Min = smaDeltaStdOutCount3Min;
    }

    public Integer getSmaDeltaStdOutCount3Max() {
        return smaDeltaStdOutCount3Max;
    }

    public void setSmaDeltaStdOutCount3Max(Integer smaDeltaStdOutCount3Max) {
        this.smaDeltaStdOutCount3Max = smaDeltaStdOutCount3Max;
    }

    public Double getSmaDeltaBackTradeMin() {
        return smaDeltaBackTradeMin;
    }

    public void setSmaDeltaBackTradeMin(Double smaDeltaBackTradeMin) {
        this.smaDeltaBackTradeMin = smaDeltaBackTradeMin;
    }

    public Double getSmaDeltaBackTradeMax() {
        return smaDeltaBackTradeMax;
    }

    public void setSmaDeltaBackTradeMax(Double smaDeltaBackTradeMax) {
        this.smaDeltaBackTradeMax = smaDeltaBackTradeMax;
    }

    @Override
    public String toString() {
        return "PairSearchRange{" +
                "correlationMin=" + correlationMin +
                ", correlationMax=" + correlationMax +
                ", volatilityMin=" + volatilityMin +
                ", volatilityMax=" + volatilityMax +
                ", pairTestMin=" + pairTestMin +
                ", pairTestMax=" + pairTestMax +
                ", pairHiLowMin=" + pairHiLowMin +
                ", pairHiLowMax=" + pairHiLowMax +
                ", smaDeltaStdOutCount1Min=" + smaDeltaStdOutCount1Min +
                ", smaDeltaStdOutCount1Max=" + smaDeltaStdOutCount1Max +
                ", smaDeltaStdOutCount2Min=" + smaDeltaStdOutCount2Min +
                ", smaDeltaStdOutCount2Max=" + smaDeltaStdOutCount2Max +
                ", smaDeltaStdOutCount3Min=" + smaDeltaStdOutCount3Min +
                ", smaDeltaStdOutCount3Max=" + smaDeltaStdOutCount3Max +
                ", smaDeltaBackTradeMin=" + smaDeltaBackTradeMin +
                ", smaDeltaBackTradeMax=" + smaDeltaBackTradeMax +
                '}';
    }
}

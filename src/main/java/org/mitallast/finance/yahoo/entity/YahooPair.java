package org.mitallast.finance.yahoo.entity;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"left_id", "right_id"}),
        indexes = {@Index(columnList = "correlation"), @Index(columnList = "volatility"), @Index(columnList = "smaDeltaBackTrade"),
                @Index(columnList = "pairTest"), @Index(columnList = "pairHiLow"),
                @Index(columnList = "smaDeltaStdOutCount1"), @Index(columnList = "smaDeltaStdOutCount2"), @Index(columnList = "smaDeltaStdOutCount3")
        }
)
public class YahooPair {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Valid
    @NotNull
    @JoinColumn
    @ManyToOne(optional = false)
    private YahooTicker left;

    @Valid
    @NotNull
    @JoinColumn
    @ManyToOne(optional = false)
    private YahooTicker right;

    @JoinColumn
    @NotNull
    @ManyToOne(optional = false)
    private YahooIndustry leftIndustry;

    @JoinColumn
    @NotNull
    @ManyToOne(optional = false)
    private YahooIndustry rightIndustry;

    private Double correlation;

    private Integer smaDeltaStdOutCount1;

    private Integer smaDeltaStdOutCount2;

    private Integer smaDeltaStdOutCount3;

    private Double smaDeltaBackTrade;

    private Double volatility;

    private Double pairTest;

    private Double pairHiLow;

    @AssertFalse
    public boolean isEqualLeftAndRight() {
        if (left == null) {
            return false;
        }
        if (right == null) {
            return false;
        }
        return left.getId() == right.getId();
    }

    @AssertTrue
    public boolean isLeftLessThanRight() {
        if (left == null) {
            return false;
        }
        if (right == null) {
            return false;
        }
        return left.getId() < right.getId();
    }

    public YahooPair() {
    }

    public YahooPair(YahooTicker left, YahooTicker right) {
        setTickers(left, right);
    }

    public String getTitle() {
        return left.getName() + ":" + right.getName();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public YahooTicker getLeft() {
        return left;
    }

    public void setLeft(YahooTicker left) {
        setTickers(left, right);
    }

    public YahooTicker getRight() {
        return right;
    }

    public void setRight(YahooTicker right) {
        setTickers(left, right);
    }

    public void setTickers(YahooTicker left, YahooTicker right) {
        if (left == null || right == null || left.getId() <= right.getId()) {
            this.left = left;
            this.right = right;
        } else {
            this.left = right;
            this.right = left;
        }
        if (this.left != null) {
            this.leftIndustry = left.getIndustry();
        }
        if (this.right != null) {
            this.rightIndustry = right.getIndustry();
        }
    }

    public Integer getSmaDeltaStdOutCount1() {
        return smaDeltaStdOutCount1;
    }

    public void setSmaDeltaStdOutCount1(Integer smaDeltaStdOutCount1) {
        this.smaDeltaStdOutCount1 = smaDeltaStdOutCount1;
    }

    public Integer getSmaDeltaStdOutCount2() {
        return smaDeltaStdOutCount2;
    }

    public void setSmaDeltaStdOutCount2(Integer smaDeltaStdOutCount) {
        this.smaDeltaStdOutCount2 = smaDeltaStdOutCount;
    }

    public Integer getSmaDeltaStdOutCount3() {
        return smaDeltaStdOutCount3;
    }

    public void setSmaDeltaStdOutCount3(Integer smaDeltaStdOutCount3) {
        this.smaDeltaStdOutCount3 = smaDeltaStdOutCount3;
    }

    public Double getSmaDeltaBackTrade() {
        return smaDeltaBackTrade;
    }

    public void setSmaDeltaBackTrade(Double smaDeltaBackTrade) {
        this.smaDeltaBackTrade = smaDeltaBackTrade;
    }

    public Double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(Double correlation) {
        this.correlation = correlation;
    }

    public Double getVolatility() {
        return volatility;
    }

    public void setVolatility(Double volatility) {
        this.volatility = volatility;
    }

    public Double getPairTest() {
        return pairTest;
    }

    public void setPairTest(Double pairTest) {
        this.pairTest = pairTest;
    }

    public YahooIndustry getLeftIndustry() {
        return leftIndustry;
    }

    public YahooIndustry getRightIndustry() {
        return rightIndustry;
    }

    public Double getPairHiLow() {
        return pairHiLow;
    }

    public void setPairHiLow(Double pairHiLow) {
        this.pairHiLow = pairHiLow;
    }

    public boolean isNew() {
        return this.id > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        YahooPair yahooPair = (YahooPair) o;

        if (id != yahooPair.id) {
            return false;
        }
        if (correlation != null ? !correlation.equals(yahooPair.correlation) : yahooPair.correlation != null) {
            return false;
        }
        if (left != null ? !left.equals(yahooPair.left) : yahooPair.left != null) {
            return false;
        }
        if (leftIndustry != null ? !leftIndustry.equals(yahooPair.leftIndustry) : yahooPair.leftIndustry != null) {
            return false;
        }
        if (pairHiLow != null ? !pairHiLow.equals(yahooPair.pairHiLow) : yahooPair.pairHiLow != null) {
            return false;
        }
        if (pairTest != null ? !pairTest.equals(yahooPair.pairTest) : yahooPair.pairTest != null) {
            return false;
        }
        if (right != null ? !right.equals(yahooPair.right) : yahooPair.right != null) {
            return false;
        }
        if (rightIndustry != null ? !rightIndustry.equals(yahooPair.rightIndustry) : yahooPair.rightIndustry != null) {
            return false;
        }
        if (smaDeltaBackTrade != null ? !smaDeltaBackTrade.equals(yahooPair.smaDeltaBackTrade) : yahooPair.smaDeltaBackTrade != null) {
            return false;
        }
        if (smaDeltaStdOutCount1 != null ? !smaDeltaStdOutCount1.equals(yahooPair.smaDeltaStdOutCount1) : yahooPair.smaDeltaStdOutCount1 != null) {
            return false;
        }
        if (smaDeltaStdOutCount2 != null ? !smaDeltaStdOutCount2.equals(yahooPair.smaDeltaStdOutCount2) : yahooPair.smaDeltaStdOutCount2 != null) {
            return false;
        }
        if (smaDeltaStdOutCount3 != null ? !smaDeltaStdOutCount3.equals(yahooPair.smaDeltaStdOutCount3) : yahooPair.smaDeltaStdOutCount3 != null) {
            return false;
        }
        if (volatility != null ? !volatility.equals(yahooPair.volatility) : yahooPair.volatility != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (left != null ? left.hashCode() : 0);
        result = 31 * result + (right != null ? right.hashCode() : 0);
        result = 31 * result + (leftIndustry != null ? leftIndustry.hashCode() : 0);
        result = 31 * result + (rightIndustry != null ? rightIndustry.hashCode() : 0);
        result = 31 * result + (correlation != null ? correlation.hashCode() : 0);
        result = 31 * result + (smaDeltaStdOutCount1 != null ? smaDeltaStdOutCount1.hashCode() : 0);
        result = 31 * result + (smaDeltaStdOutCount2 != null ? smaDeltaStdOutCount2.hashCode() : 0);
        result = 31 * result + (smaDeltaStdOutCount3 != null ? smaDeltaStdOutCount3.hashCode() : 0);
        result = 31 * result + (smaDeltaBackTrade != null ? smaDeltaBackTrade.hashCode() : 0);
        result = 31 * result + (volatility != null ? volatility.hashCode() : 0);
        result = 31 * result + (pairTest != null ? pairTest.hashCode() : 0);
        result = 31 * result + (pairHiLow != null ? pairHiLow.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YahooPair{" +
                "id=" + id +
                ", left=" + left +
                ", right=" + right +
                ", leftIndustry=" + leftIndustry +
                ", rightIndustry=" + rightIndustry +
                ", correlation=" + correlation +
                ", smaDeltaStdOutCount1=" + smaDeltaStdOutCount1 +
                ", smaDeltaStdOutCount2=" + smaDeltaStdOutCount2 +
                ", smaDeltaStdOutCount3=" + smaDeltaStdOutCount3 +
                ", smaDeltaBackTrade=" + smaDeltaBackTrade +
                ", volatility=" + volatility +
                ", pairTest=" + pairTest +
                ", pairHiLow=" + pairHiLow +
                '}';
    }
}

package org.mitallast.finance.yahoo.entity;

import com.googlecode.jcsv.annotations.MapToColumn;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class YahooTicker {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    @MapToColumn(column = 0)
    private String name;

    private boolean blacklist = false;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn
    @MapToColumn(column = 1)
    private YahooIndustry industry;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ticker")
    private Set<YahooQuote> tickers = new HashSet<>();

    public YahooTicker() {
    }

    public YahooTicker(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBlacklist() {
        return blacklist;
    }

    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
    }

    public YahooIndustry getIndustry() {
        return industry;
    }

    public void setIndustry(YahooIndustry industry) {
        this.industry = industry;
    }

    public Set<YahooQuote> getTickers() {
        return tickers;
    }

    public void setTickers(Set<YahooQuote> tickers) {
        this.tickers = tickers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        YahooTicker ticker = (YahooTicker) o;

        if (blacklist != ticker.blacklist) {
            return false;
        }
        if (id != ticker.id) {
            return false;
        }
        if (industry != null ? !industry.equals(ticker.industry) : ticker.industry != null) {
            return false;
        }
        if (name != null ? !name.equals(ticker.name) : ticker.name != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (blacklist ? 1 : 0);
        result = 31 * result + (industry != null ? industry.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YahooTicker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blacklist=" + blacklist +
                ", industry=" + industry +
                '}';
    }
}

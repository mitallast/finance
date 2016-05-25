package org.mitallast.finance.yahoo.entity;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class YahooIndustry {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "industry")
    private Set<YahooTicker> tickers = new HashSet<>();

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

    public Set<YahooTicker> getTickers() {
        return tickers;
    }

    public void setTickers(Set<YahooTicker> tickers) {
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

        YahooIndustry that = (YahooIndustry) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "YahooIndustry{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
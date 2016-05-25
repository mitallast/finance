package org.mitallast.finance.yahoo.entity;

import com.googlecode.jcsv.annotations.MapToColumn;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class YahooQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @MapToColumn(column = 0)
    private Date date;
    @MapToColumn(column = 1)
    private double open;
    @MapToColumn(column = 2)
    private double high;
    @MapToColumn(column = 3)
    private double low;
    @MapToColumn(column = 4)
    private double close;
    @MapToColumn(column = 5)
    private double volume;
    @MapToColumn(column = 6)
    private double adjClose;

    @NotNull
    @Valid
    @ManyToOne(optional = false)
    @JoinColumn
    private YahooTicker ticker;

    public YahooQuote() {
    }

    public YahooQuote(Date date, double adjClose) {
        this.date = date;
        this.adjClose = adjClose;
    }

    public Date getDate() {
        return date;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public YahooTicker getTicker() {
        return ticker;
    }

    public void setTicker(YahooTicker ticker) {
        this.ticker = ticker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        YahooQuote that = (YahooQuote) o;

        if (Double.compare(that.adjClose, adjClose) != 0) {
            return false;
        }
        if (Double.compare(that.close, close) != 0) {
            return false;
        }
        if (Double.compare(that.high, high) != 0) {
            return false;
        }
        if (Double.compare(that.low, low) != 0) {
            return false;
        }
        if (Double.compare(that.open, open) != 0) {
            return false;
        }
        if (Double.compare(that.volume, volume) != 0) {
            return false;
        }
        if (date != null ? !date.equals(that.date) : that.date != null) {
            return false;
        }
        if (ticker != null ? !ticker.equals(that.ticker) : that.ticker != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = date != null ? date.hashCode() : 0;
        temp = Double.doubleToLongBits(open);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(high);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(low);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(close);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(volume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(adjClose);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (ticker != null ? ticker.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YahooQuote{" +
                "id=" + id +
                ", date=" + date +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", adjClose=" + adjClose +
                ", ticker=" + ticker +
                "}";
    }
}

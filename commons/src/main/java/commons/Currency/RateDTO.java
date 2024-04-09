package commons.Currency;

import java.text.SimpleDateFormat;
import java.util.*;

public class RateDTO {
    public String currencyFrom;
    public String currencyTo;
    public Double rate;
    public String date;

    public RateDTO(String currencyFrom, String currencyTo, Double rate, Date date) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        this.date = dateFormat.format(date);
    }

    public RateDTO() {
    }

    public RateDTO(Rate rate) {
        this.currencyFrom = rate.currencyFrom;
        this.currencyTo = rate.currencyTo;
        this.rate = rate.rate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        this.date = dateFormat.format(rate.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RateDTO rateDTO)) return false;
        return Objects.equals(currencyFrom, rateDTO.currencyFrom) &&
                Objects.equals(currencyTo, rateDTO.currencyTo) &&
                Objects.equals(rate, rateDTO.rate) &&
                Objects.equals(date, rateDTO.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyFrom, currencyTo, rate, date);
    }
}

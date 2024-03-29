package commons.Currency;

import java.util.Date;
import java.util.Objects;

public class Rate {
    public String currencyFrom;
    public String currencyTo;

    public double rate;
    public Date date;

    public Rate(String currencyFrom, String currencyTo, double rate, Date date) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rate = rate;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rate)) return false;
        Rate rate1 = (Rate) o;
        return Double.compare(rate, rate1.rate) == 0 &&
                Objects.equals(currencyFrom, rate1.currencyFrom) &&
                Objects.equals(currencyTo, rate1.currencyTo) &&
                Objects.equals(date, rate1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyFrom, currencyTo, rate, date);
    }

    public Object getDate() {
        return date;
    }
}

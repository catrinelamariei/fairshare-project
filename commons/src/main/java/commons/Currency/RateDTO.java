package commons.Currency;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}

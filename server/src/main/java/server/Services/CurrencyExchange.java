package server.Services;

import commons.Currency.Rate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CurrencyExchange {
    private Set<Rate> currencies = new HashSet<Rate>();

    public CurrencyExchange() throws MalformedURLException {

    }

    public Rate getRate(String currencyFrom, String currencyTo,Date date)
            throws IOException, InterruptedException {

        Optional<Rate> r = currencies.stream()
                .filter(rate -> rate.currencyFrom.equals(currencyFrom) &&
                        rate.currencyTo.equals(currencyTo) &&
                        isSameDay(rate.date, date)
                )
                .findFirst();
        if (r.isEmpty()) {
            //fetch from API
            //add to currencies

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);

            String url = FrankfurterAPI.getURL(currencyFrom, currencyTo, strDate);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build(), HttpResponse.BodyHandlers.ofString());

            Double rate = FrankfurterAPI.getRate(response);

            Rate result = new Rate(currencyFrom, currencyTo, rate, date);
            currencies.add(result);
            return result;


        }
        return r.get();
    }




    private static boolean isSameDay(Date date1, Date date2) {

        // Strip out the time part of each date.
        final long MILLIS_PER_DAY = 86400000;
        long julianDayNumber1 = date1.getTime() / MILLIS_PER_DAY;
        long julianDayNumber2 = date2.getTime() / MILLIS_PER_DAY;

        // If they now are equal then it is the same day.
        return julianDayNumber1 == julianDayNumber2;
    }


}

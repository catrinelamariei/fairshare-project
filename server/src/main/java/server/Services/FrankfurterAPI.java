package server.Services;

import java.net.http.HttpResponse;

public class FrankfurterAPI {
    public static String getURL(String currencyFrom, String currencyTo, String date) {
        return "https://api.frankfurter.app/" + date + "?from=" + currencyFrom + "&to=" + currencyTo;
    }

    public static Double getRate(HttpResponse<String> response) {
        String[] parts = response.body().split(",");
        String rate = parts[3].split(":")[2];
        rate = rate.substring(0, rate.length() - 2);
        return Double.parseDouble(rate);
    }
}

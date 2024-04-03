package server.Services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class FrankfurterAPI {
    public String getURL(String currencyFrom, String currencyTo, String date) {
        return "https://api.frankfurter.app/" + date + "?from=" + currencyFrom + "&to=" + currencyTo;
    }

    public Double getRate(String url) {

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = null;
        try {
            response = client.send(HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build(), HttpResponse.BodyHandlers.ofString());

            String[] parts = response.body().split(",");
            String rate = parts[3].split(":")[2];
            rate = rate.substring(0, rate.length() - 2);
            return Double.parseDouble(rate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

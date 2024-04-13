package server.Services;

import commons.Currency.Rate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.*;
import java.text.*;
import java.util.*;

@Service
public class CurrencyExchange {
    private final Set<Rate> currencies = new HashSet<>();
    private final FrankfurterAPI api;

    public CurrencyExchange(FrankfurterAPI api) {
        this.api = api;
    }

    public Rate getRate(String currencyFrom, String currencyTo,Date date) {

        try {
            Files.createDirectories(Paths.get("server/src/main/resources/rates/"));
        } catch (IOException e) {
            System.err.println("Cannot create directories: " + e);
        }

        Optional<Rate> r = currencies.stream()
                .filter(rate -> rate.currencyFrom.equals(currencyFrom) &&
                        rate.currencyTo.equals(currencyTo) &&
                        isSameDay(rate.date, date)
                )
                .findFirst();
        if (r.isEmpty()) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(date);
            String url = api.getURL(currencyFrom, currencyTo, strDate);
            Double rate = api.getRate(url);
            Rate result = new Rate(currencyFrom, currencyTo, rate, date);
            currencies.add(result);

            String fileName =  "server/src/main/resources/rates/"
                    + currencyFrom + "_to_"+ currencyTo+ ".txt";
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(rate.toString());
                writer.close();
            } catch (IOException e) {
                System.out.println("Cannot write to rates folder");
            }
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


    public Rate getRateFromFile(String currencyFrom, String currencyTo, String date)
            throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date parsedDate = dateFormat.parse(date);
        String fileName =  "server/src/main/resources/rates/"
                +currencyFrom+ "_to_"+currencyTo+".txt";
        File file = new File(fileName);
        if(!file.exists()){
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write("1.0");
                writer.close();
                return new Rate(currencyFrom, currencyTo, 1.0, parsedDate);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        try {
            Scanner scanner = new Scanner(file);
            String rate = scanner.nextLine();
            scanner.close();
            return new Rate(currencyFrom, currencyTo, Double.parseDouble(rate), parsedDate);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public BigDecimal getAmount(String currencyCode, BigDecimal amount, Date date) {
        if(!currencyCode.equals("EUR")){
            //convert to EUR
            try {
                amount = amount.multiply(new BigDecimal(
                        this.getRate(currencyCode, "EUR", date).rate));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return amount;
    }
}

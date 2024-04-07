package client.utils;

import client.UserData;
import commons.Currency.RateDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class RateUtils {

    public static RateDTO getRate(String from, String to,
                                  Date date) throws WebApplicationException {


        if(from.equals(to))
            return new RateDTO(from, to, 1.0, date);

        RateDTO rate;
        rate = new RateDTO("EUR", to,0.0, date);

        try {
            Files.createDirectories(Paths.get("client/src/main/resources/client/rates"));
        } catch (IOException e) {
            System.err.println("Cannot create directories: " + e);
        }
        String filePath = "client/src/main/resources/client/rates/"
                + from + to + rate.date + ".txt";
        File file = new File(filePath);
        if(file.exists()){

            try {
                Scanner scanner = new Scanner(file);

                String r = scanner.nextLine();
                scanner.close();
                rate = new RateDTO(from, to, Double.parseDouble(r), date);
                return rate;
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + e);
            }
        }

        RateDTO result =  ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/rate/")
                .request(APPLICATION_JSON)
                .post(Entity.entity(rate, APPLICATION_JSON),RateDTO.class);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(result.rate.toString());
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}

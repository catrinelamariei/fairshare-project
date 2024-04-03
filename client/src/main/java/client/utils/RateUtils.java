package client.utils;

import client.UserData;
import commons.Currency.RateDTO;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;

import java.util.Date;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class RateUtils {

    public static RateDTO getRate(String from, String to,
                                  Date date) throws WebApplicationException {

        if(from.equals(to))
            return new RateDTO(from, to, 1.0, date);
        RateDTO rate;
        rate = new RateDTO("EUR", to,0.0, date);
        return ClientBuilder.newClient()
                .target(UserData.getInstance().getServerURL()).path("api/rate/")
                .request(APPLICATION_JSON)
                .post(Entity.entity(rate, APPLICATION_JSON),RateDTO.class);
    }
}

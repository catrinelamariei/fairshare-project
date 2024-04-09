package server.api;

import commons.Currency.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.CurrencyExchange;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/api/rate")
public class RateController {

    private final CurrencyExchange currencyExchange;
    public RateController(CurrencyExchange currencyExchange){
        this.currencyExchange = currencyExchange;
    }

    @PostMapping("/")
    public ResponseEntity<RateDTO> getRate(@RequestBody RateDTO r) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Date parsedDate = dateFormat.parse(r.date);
            Rate result = currencyExchange.getRate(r.currencyFrom, r.currencyTo, parsedDate);
            RateDTO resultDTO = new RateDTO(result);
            return ResponseEntity.ok(resultDTO);
        } catch (Exception e) {
            try {
                //read from file and return
                Rate result = currencyExchange
                        .getRateFromFile(r.currencyFrom, r.currencyTo, r.date);
                RateDTO resultDTO = new RateDTO(result);
                return ResponseEntity.ok(resultDTO);
            } catch (Exception ex) {
                return ResponseEntity.badRequest().build();
            }

        }

    }
}

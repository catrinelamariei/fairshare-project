package server.api;

import commons.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TransactionRepository;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class TransactionControllerTest {

    private TransactionController tc;
    private TransactionRepository repo;

    @BeforeEach
    public void setup(){
        tc = new TransactionController(repo);
    }

    @Test
    void createNullTransaction() {
        Transaction t = null;
        var actual = tc.createTransaction(t);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void createNullAmountTransaction() {
        Date d = new Date(2004, 1, 22);
        String s = "";
        BigDecimal amount = null;
        Transaction t = new Transaction(d,s,amount);
        var actual = tc.createTransaction(t);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void createZeroAmountTransaction() {
        Date d = new Date(2004, 1, 22);
        String s = "";
        BigDecimal amount = BigDecimal.ZERO;
        Transaction t = new Transaction(d,s,amount);
        var actual = tc.createTransaction(t);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void getTransactionById() {
    }

    @Test
    void updateById() {
    }

    @Test
    void deleteById() {
    }
}
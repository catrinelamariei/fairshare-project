package server.api;

import commons.DTOs.TransactionDTO;
import commons.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import server.database.TransactionRepository;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class TransactionControllerTest {

//    @Autowired
    private TransactionController tc;
    private TransactionRepository repo;

    @BeforeEach
    public void setup() {
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
//        Date d = new Date(2004, 1, 22);
//        String s = "";
//        BigDecimal amount = BigDecimal.valueOf(2222);
//        Transaction t = new Transaction(d,s,amount);
//        tc.createTransaction(t);
//        t.setAmount(BigDecimal.valueOf(1111));
//        tc.updateById(t.getId(),t);
//        ResponseEntity<TransactionDTO> tr = tc.getTransactionById(t.getId());
//        assertEquals(tr.getBody().amount,BigDecimal.valueOf(1111));

    }

    @Test
    void deleteById() {
    }
}
package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.TransactionDTO;
import commons.Event;
import commons.Participant;
import commons.Transaction;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.dependencies.TestTransactionRepository;
import server.database.TransactionRepository;
import static org.springframework.http.HttpStatus.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

class TransactionControllerTest {

//    @Autowired
    private TransactionController ctrl;
    private TransactionRepository repo;
    TransactionDTO tsDTO1;
    Transaction ts1;
    Transaction ts2;

    @BeforeEach
    void setUp() {
        repo = new TestTransactionRepository();
        ctrl = new TransactionController(repo);
        Event event = new Event("event");
        tsDTO1 = new TransactionDTO(
                new UUID(0,0),
                new Date(), "eur",
                new BigDecimal("5.20"),
                new Participant(event,"Clay", "Smith", "mail0", "iban0"));

        ts1 = new Transaction(
                event,
                new Date(), "usd",
                new BigDecimal("6.79"),
                new Participant(event,"Max", "Well", "mail1", "iban1"));
        ts1.id = new UUID(0,1);
        ctrl.createTransaction(ts1);

        ts2 = new Transaction(
                new Event("event"),
                new Date(), "usd",
                new BigDecimal("0.00"),
                new Participant(event,"Bob", "Snow", "email2", "iban"));
        ts2.id = new UUID(0,2);
    }

    @Test
    void createNullTransaction() {
        Transaction t = null;
        var actual = ctrl.createTransaction(t);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void createNullAmountTransaction() {
        Date d = new Date(2004, 1, 22);
        String s = "";
        BigDecimal amount = null;
        Event ev = new Event("event");
        Participant author = new Participant(ev,"Frey", "Port", "mail3", "iban3");
        Transaction t = new Transaction(ev,d,s,amount, author);
        var actual = ctrl.createTransaction(t);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    void createNegativeAmountTransaction() {
        Date d = new Date(2004, 1, 22);
        String s = "";
        Event event = new Event("event");
        BigDecimal amount = new BigDecimal("-19.99");
        Participant author = new Participant(event,"Frey", "Port", "mail3", "iban3");
        Transaction t = new Transaction(event, d,s,amount, author);
        var actual = ctrl.createTransaction(t);
        assertEquals(BAD_REQUEST, actual.getStatusCode());
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

    //post success
    @Test
    public void postSuccess() {
        assertEquals(OK, ctrl.createTransaction(ts2).getStatusCode());
    }
    //post fail
    @Test
    public void postFail() {
        ts2.id = null;
        assertEquals(BAD_REQUEST, ctrl.createTransaction(ts2).getStatusCode());
    }

    //get success
    @Test
    public void getSucces() {
        ResponseEntity<TransactionDTO> response = ctrl.getTransactionById(ts1.id);
        assertEquals(ts1, new Transaction(response.getBody()));
        assertEquals(OK, response.getStatusCode());
    }
    //get fail
    @Test
    public void getFail() {
        assertEquals(NOT_FOUND,
                ctrl.getTransactionById(new UUID(123, 456)).getStatusCode());
    }

    //put success
    @Test
    public void putSucces() {
        ResponseEntity<TransactionDTO> response = ctrl.updateTransactionById(ts1.id, ts2);
        assertEquals(ts2.amount, response.getBody().amount);
        assertEquals(OK, response.getStatusCode());
    }
    //put fail
    @Test
    public void putFail() {
        assertEquals(NOT_FOUND,
                ctrl.updateTransactionById(new UUID(123, 456), ts2)
                        .getStatusCode());
    }

    //special put
    @Test
    public void specialPutTest() {
        ts1.amount = new BigDecimal("100");

        ResponseEntity<TransactionDTO> response = ctrl.updateTransaction(ts1);
        assertEquals(ts1, new Transaction(response.getBody()));
        assertEquals(OK, response.getStatusCode());
    }

    //delete success
    @Test
    public void deleteSuccess() {
        ResponseEntity<TransactionDTO> response = ctrl.deleteTransactionById(ts1.id);
        assertEquals(ts1, new Transaction(response.getBody()));
        assertEquals(OK, response.getStatusCode());
    }

    //delete fail
    @Test
    public void deleteFail() {
        assertEquals(NOT_FOUND,
                ctrl.deleteTransactionById(new UUID(123, 456)).getStatusCode());
    }

}
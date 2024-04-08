package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import commons.Event;
import commons.Participant;
import commons.Tag;
import commons.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.Services.DTOtoEntity;
import server.database.ParticipantRepository;
import server.database.TransactionRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {
    private TransactionController controller;
    private TransactionRepository repo = mock(TransactionRepository.class);
    private DTOtoEntity d2e = mock(DTOtoEntity.class);
    private SimpMessagingTemplate smtMock = mock(SimpMessagingTemplate.class);
    private Transaction transaction;
    private TransactionDTO transactionDTO;

    @BeforeEach
    public void setUp() {
        controller = new TransactionController(repo,d2e,smtMock);
        Event event = new Event("event");
        event.id = UUID.randomUUID();
        Participant participant = new Participant(event, "participant",
                "last name", "email", "iban","bic");
        transaction = new Transaction(event, new Date(),"eur",new BigDecimal(100),
                participant, "description");
        transaction.id = UUID.randomUUID();
        transactionDTO = new TransactionDTO(transaction);
        when(d2e.get(any(EventDTO.class))).thenReturn(event);
    }

    @Test
    public void getTransaction() {
        when(repo.existsById(transaction.id)).thenReturn(true);
        when(repo.getReferenceById(transaction.id)).thenReturn(transaction);
        ResponseEntity<TransactionDTO> response = controller.getTransaction(transaction.id);
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    public void getTransactionNotFound() {
        when(repo.existsById(transaction.id)).thenReturn(false);
        ResponseEntity<TransactionDTO> response = controller.getTransaction(transaction.id);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void createTransaction() {
        transactionDTO.id = null;
        when(d2e.create(transactionDTO)).thenReturn(transaction);
        ResponseEntity<TransactionDTO> response = controller.createTransaction(transactionDTO);
        transactionDTO.id = transaction.id;
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    public void createTransactionBadRequest() {
        transactionDTO.id = null;
        transactionDTO.amount = new BigDecimal(-1);
        when(d2e.create(transactionDTO)).thenReturn(transaction);
        ResponseEntity<TransactionDTO> response = controller.createTransaction(transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);
    }

    @Test
    public void updateTransaction() {
        when(repo.existsById(transaction.id)).thenReturn(true);
        when(d2e.update(transactionDTO)).thenReturn(transaction);
        ResponseEntity<TransactionDTO> response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    public void updateTransactionNotFound() {
        when(repo.existsById(transaction.id)).thenReturn(false);
        ResponseEntity<TransactionDTO> response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void updateTransactionBadRequest() {
        transactionDTO.amount = new BigDecimal(-1);
        ResponseEntity<TransactionDTO> response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        transactionDTO.amount = new BigDecimal(100);
        transactionDTO.currencyCode = null;
        response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        transactionDTO.currencyCode = "";
        response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        transactionDTO.currencyCode = "eur";
        transactionDTO.subject = null;
        response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        transactionDTO.subject = "";
        response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        transactionDTO.subject = "description";
        transactionDTO.eventId = null;
        response = controller.updateTransaction(transaction.id, transactionDTO);
        assertEquals(ResponseEntity.badRequest().build(), response);

        response = controller.updateTransaction(transaction.id, null);
        assertEquals(ResponseEntity.badRequest().build(), response);

    }

    @Test
    public void updateTransactionById() {
        when(repo.existsById(transaction.id)).thenReturn(true);
        when(d2e.update(transactionDTO)).thenReturn(transaction);
        ResponseEntity<TransactionDTO> response = controller.updateTransaction(transactionDTO);
        assertEquals(transactionDTO, response.getBody());
    }

    @Test
    public void deleteTransactionById() {
        when(repo.existsById(transaction.id)).thenReturn(true);
        when(repo.getReferenceById(transaction.id)).thenReturn(transaction);
        ResponseEntity<TransactionDTO> response = controller.deleteTransactionById(transaction.id);
        verify(repo).delete(transaction);
    }

    @Test
    public void deleteTransactionByIdNotFound() {
        when(repo.existsById(transaction.id)).thenReturn(false);
        ResponseEntity<TransactionDTO> response = controller.deleteTransactionById(transaction.id);
        assertEquals(ResponseEntity.notFound().build(), response);
    }

    @Test
    public void deleteTransactionByIdBadRequest() {
        ResponseEntity<TransactionDTO> response = controller.deleteTransactionById(null);
        assertEquals(ResponseEntity.badRequest().build(), response);
    }
}
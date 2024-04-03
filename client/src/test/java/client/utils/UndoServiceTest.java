package client.utils;

import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.TransactionNode;
import commons.DTOs.ParticipantDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static client.utils.UndoService.TsAction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UndoServiceTest {
    //services
    private EventPageCtrl eventPageCtrl;
    private ServerUtils server;
    private UndoService undoService;

    //objects
    private TransactionDTO ts1;
    private TransactionDTO ts2;
    private ObservableList<Node> children;

    @BeforeEach
    public void setUp() throws Exception {
        //services
        this.eventPageCtrl = mock(EventPageCtrl.class);
        this.eventPageCtrl.transactions = mock(VBox.class);
        this.server = mock(ServerUtils.class);
        this.undoService = new UndoService(eventPageCtrl, server);


        //objects
        ParticipantDTO p = new ParticipantDTO(new UUID(0,2), new UUID(0,0),
            "Max", "Well", "mw@me.com", "-", "-");

        ts1 = new TransactionDTO(new UUID(0, 1), new UUID(0,0), new Date(), "eur",
            new BigDecimal("19.99"), p, new HashSet<ParticipantDTO>(List.of(p)),
            new HashSet<TagDTO>(List.of(new TagDTO())), "Burger");

        ts2 = new TransactionDTO(new UUID(0, 1), new UUID(0,0), new Date(), "eur",
            new BigDecimal("29.99"), p, new HashSet<ParticipantDTO>(List.of(p)),
            new HashSet<TagDTO>(List.of(new TagDTO())), "Burger");

        children = FXCollections.observableArrayList();


        //mocking
        when(eventPageCtrl.transactions.getChildren())
            .thenReturn(children);

        //will be replaced using factory pattern
    }

    //Test doesn't do much, just checks all cases are covered
    @Test
    public void addActionTest() {
        undoService.addAction(CREATE, ts1);
        undoService.addAction(UPDATE, ts1);
        undoService.addAction(DELETE, ts2);
    }

    @Test
    public void illegalAddActionTest() {
        assertThrows(IllegalArgumentException.class,
            () -> undoService.addAction(CREATE, null));
    }


    @Test
    public void noActionUndoTest() {
        assertFalse(undoService.undo());
    }

    @Test
    public void undoTest() {
        undoService.addAction(CREATE, ts1);
        assertTrue(undoService.undo());
    }

    @Test
    public void clearTest() {
        undoService.addAction(CREATE, ts1);
        undoService.clear();
        assertFalse(undoService.undo());
    }

    //see if constructor is actually being mocked
    @Test
    public void transactionNodeCreateTest() throws Exception {
        // TODO: make factory and use that
    }

    @Test
    public void undoCreateTest() {
        doNothing().when(server).deleteTransaction(ts1.id);

        children.add(new TransactionNode(ts1, eventPageCtrl, server)); // TODO use factory
        undoService.addAction(CREATE, ts1);
        undoService.undo();

        verify(eventPageCtrl.transactions).getChildren();
        verify(server).deleteTransaction(ts1.id);
        assertTrue(eventPageCtrl.transactions.getChildren().isEmpty());
    }

    @Test
    public void undoUpdateTest() {
        doNothing().when(server).putTransaction(ts1);

        undoService.addAction(UPDATE, ts1);
        undoService.undo();

    }

    @Test
    public void undoDeleteTest() {
        doNothing().when(server).postTransaction(ts1);

        undoService.addAction(DELETE, ts1);
        undoService.undo();

    }
}

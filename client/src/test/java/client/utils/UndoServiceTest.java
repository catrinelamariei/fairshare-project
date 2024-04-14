package client.utils;

import client.*;
import client.scenes.EventPageCtrl;
import client.scenes.javaFXClasses.DataNode.*;
import client.scenes.javaFXClasses.NodeFactory;
import com.google.inject.Provider;
import commons.DTOs.*;
import javafx.collections.*;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;

import static client.utils.UndoService.TsAction.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UndoServiceTest {
    //services
    private EventPageCtrl eventPageCtrl;
    private ServerUtils server;
    private NodeFactory nodeFactory;
    private UndoService undoService;

    //objects
    private TransactionDTO ts1;
    private TransactionDTO ts2;
    private TransactionDTO ts3;
    private ObservableList<Node> children;

    @BeforeEach
    public void setUp() throws Exception {
        //services
        this.eventPageCtrl = mock(EventPageCtrl.class);
        this.eventPageCtrl.transactions = mock(VBox.class);
        this.server = mock(ServerUtils.class);
        this.nodeFactory = spy(new PojoNodeFactory(mock(MainCtrl.class), eventPageCtrl, server,
            spy(new EventJsonUtil(server)), UserData.load()));
        this.undoService = new UndoService(new TestProvider<>(eventPageCtrl), server, nodeFactory);


        //objects
        ParticipantDTO p = new ParticipantDTO(new UUID(0,1), new UUID(0,0),
            "Max", "Well", "mw@me.com", "-", "-");

        ts1 = new TransactionDTO(new UUID(0, 2), new UUID(0,0), new Date(), "eur",
            new BigDecimal("19.99"), p, new HashSet<ParticipantDTO>(List.of(p)),
            new HashSet<TagDTO>(List.of(new TagDTO())), "Burger");

        ts2 = new TransactionDTO(new UUID(0, 2), new UUID(0,0), new Date(), "eur",
            new BigDecimal("29.99"), p, new HashSet<ParticipantDTO>(List.of(p)),
            new HashSet<TagDTO>(List.of(new TagDTO())), "Burger");

        ts3 = new TransactionDTO(new UUID(0, 3), new UUID(0,0), new Date(), "eur",
                new BigDecimal("29.99"), p, new HashSet<ParticipantDTO>(List.of(p)),
                new HashSet<TagDTO>(List.of(new TagDTO())), "Burger");

        children = FXCollections.observableArrayList();


        //mocking
        when(eventPageCtrl.transactions.getChildren()).thenReturn(children);
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
        TransactionNode tsn = nodeFactory.createTransactionNode(ts1);
        assertEquals(tsn.id, ts1.id);
    }

    @Test
    public void undoCreateTest() {
        doNothing().when(server).deleteTransaction(ts1.id);

        children.add(nodeFactory.createTransactionNode(ts1)); // TODO use factory
        undoService.addAction(CREATE, ts1);
        undoService.undo();

        verify(eventPageCtrl.transactions).getChildren();
        verify(server).deleteTransaction(ts1.id);
        assertTrue(eventPageCtrl.transactions.getChildren().isEmpty());
    }

    @Test
    public void undoUpdateTest() {
        when(server.putTransaction(ts1)).thenReturn(ts1);

        children.add(nodeFactory.createTransactionNode(ts2)); //contains updated ts
        undoService.addAction(UPDATE, ts1);
        undoService.undo();

        verify(server).putTransaction(ts1);
        verify(nodeFactory).createTransactionNode(ts1);
    }

    @Test
    public void undoDeleteTest() {
        when(server.postTransaction(ts1)).thenReturn(ts2); //new uuid

        undoService.addAction(DELETE, ts1);
        undoService.undo();

        verify(server).postTransaction(ts1);
        assertEquals(((TransactionNode) children.getFirst()).id, ts2.id);
    }

    /**
     * this test checks if the ids update after a new UUID is returned
     * from the server so that older undo actions remain valid.
     * This test fails if the ID is not updated causing a .get() on empty optional
     * which will throw NoSuchElementException
     */
    @Test
    public void undoUpdateDeleteTest() {
        when(server.postTransaction(ts2)).thenReturn(ts3); //new UUID
        when(server.putTransaction(ts1)).thenReturn(ts3); //ts1's UUID will have been changed

        undoService.addAction(UPDATE, ts1); //old ts
        undoService.addAction(DELETE, ts2); //deleted new ts

        undoService.undo(); //restore ts2
        undoService.undo(); //revert ts2 to ts1 (with new id)

        assertEquals(((TransactionNode) children.getFirst()).id, ts3.id);
        assertEquals(ts1.id, ts3.id);
    }

    private class TestProvider<T> implements Provider<T> {
        private final T val;

        protected TestProvider(T val) {
            this.val = val;
        }
        @Override
        public T get() {
            return val;
        }
    }
}

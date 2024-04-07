package client.scenes;

import client.UserData;
import client.utils.ServerUtils;
import commons.DTOs.*;
import javafx.scene.text.Text;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventPageCtrlTest {

    @Mock
    private ServerUtils server;

    @InjectMocks
    private EventPageCtrl eventPageCtrl;

    private UUID mockId;
    private EventDTO eventDTO;
    private TransactionDTO transactionDTO;
    private TagDTO tagDTO;
    private ArrayDeque deque;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        eventDTO = Mockito.mock(EventDTO.class);
        mockId = UUID.randomUUID(); // Generate a random UUID for testing
        transactionDTO = Mockito.mock(TransactionDTO.class);
        tagDTO = Mockito.mock(TagDTO.class);
        deque = new ArrayDeque<>();
        deque.add(new UserData.Pair<>(mockId, "Mock Event"));
        eventPageCtrl.totalExpenses = new Text();

        when(server.getEvent(mockId)).thenReturn(eventDTO);
        when(eventDTO.getTransactions()).thenReturn(new HashSet<>(Arrays.asList(transactionDTO)));
        when(transactionDTO.getTags()).thenReturn(new HashSet<>(Arrays.asList(tagDTO)));
        when(transactionDTO.getAmount()).thenReturn(BigDecimal.valueOf(100.0));
        UserData.getInstance().setRecentUUIDs(deque);
    }

    @Test
    public void testUpdateTotalExpenses() {
        when(tagDTO.getName()).thenReturn("tagname");
        eventPageCtrl.updateTotalExpenses();

        assertEquals("100.0", eventPageCtrl.totalExpenses.getText());
    }

    @Test
    public void testNoUpdatesForDebts() {
        when(tagDTO.getName()).thenReturn("debt");
        eventPageCtrl.updateTotalExpenses();

        assertEquals("0.0", eventPageCtrl.totalExpenses.getText());
    }
}
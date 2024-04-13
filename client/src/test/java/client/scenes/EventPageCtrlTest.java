package client.scenes;
import client.UserData;
import client.utils.ServerUtils;
import commons.DTOs.*;
import javafx.scene.text.Text;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EventPageCtrlTest {

    @Mock
    private ServerUtils server;
    @Spy
    private UserData userData;

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
        eventDTO = mock(EventDTO.class);
        mockId = UUID.randomUUID(); // Generate a random UUID for testing
        transactionDTO = mock(TransactionDTO.class);
        tagDTO = mock(TagDTO.class);
        eventPageCtrl.eventCostFiltered = new Text();

        when(server.getEvent(mockId)).thenReturn(eventDTO);
        when(eventDTO.getTransactions()).thenReturn(new HashSet<>(Arrays.asList(transactionDTO)));
        when(transactionDTO.getTags()).thenReturn(new HashSet<>(Arrays.asList(tagDTO)));
        when(transactionDTO.getAmount()).thenReturn(BigDecimal.valueOf(100.0));
        userData.getRecentUUIDs().clear();
        userData.getRecentUUIDs().add(new UserData.Pair<>(mockId, "Mock Event"));
    }

    @Test
    public void testUpdateTotalExpenses() {
        when(tagDTO.getName()).thenReturn("tagname");
        eventPageCtrl.updateTotalExpenses();

        assertEquals("\u20AC 100.0", eventPageCtrl.eventCostFiltered.getText());
    }

    @Test
    public void testNoUpdatesForDebts() {
        when(tagDTO.getName()).thenReturn("debt");
        eventPageCtrl.updateTotalExpenses();

        assertEquals("\u20AC 0.0", eventPageCtrl.eventCostFiltered.getText());
    }
}
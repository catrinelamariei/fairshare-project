package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.UserDataInterface;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventPageCtrlTest {

    @Mock
    private ServerUtils server;

    @Mock
    private MainCtrl mainCtrl;

    @Mock
    private UserDataInterface userData;

    @InjectMocks
    private EventPageCtrl eventPageCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateTotalExpenses() {
        EventDTO eventDTO = Mockito.mock(EventDTO.class);
        UUID mockId = UUID.randomUUID(); // Generate a random UUID for testing
        TransactionDTO transactionDTO = Mockito.mock(TransactionDTO.class);
        TagDTO tagDTO = Mockito.mock(TagDTO.class);
        ArrayDeque<UserData.Pair<UUID, String>> deque = new ArrayDeque<>();
        deque.add(new UserData.Pair<>(mockId, "Mock Event"));

        when(server.getEvent(mockId)).thenReturn(eventDTO);
        when(userData.getCurrentUUID()).thenReturn(mockId);
        when(userData.getRecentUUIDs()).thenReturn(deque);
        when(eventDTO.getTransactions()).thenReturn(new HashSet<>(Arrays.asList(transactionDTO)));
        when(transactionDTO.getTags()).thenReturn(new HashSet<>(Arrays.asList(tagDTO)));
        when(tagDTO.getName()).thenReturn("tagname");
        when(transactionDTO.getAmount()).thenReturn(BigDecimal.valueOf(100.0));

        eventPageCtrl.updateTotalExpenses();

        assertEquals("100.0", eventPageCtrl.totalExpenses.getText());
    }
}
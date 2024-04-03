package client.scenes;

import client.MainCtrl;
import client.UserData;
import client.UserDataInterface;
import client.utils.ServerUtils;
import commons.DTOs.EventDTO;
import commons.DTOs.TagDTO;
import commons.DTOs.TransactionDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class EventPageCtrlTest {


    @Test
    public void testUpdateTotalExpenses() {
        ServerUtils server = Mockito.mock(ServerUtils.class);
        MainCtrl mainCtrl = Mockito.mock(MainCtrl.class);
        EventPageCtrl eventPageCtrl = new EventPageCtrl(server, mainCtrl);
        UserDataInterface userData = Mockito.mock(UserDataInterface.class);
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

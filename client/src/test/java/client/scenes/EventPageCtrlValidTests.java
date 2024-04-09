package client.scenes;

import client.UserData;
import client.scenes.javaFXClasses.NodeFactory;
import client.utils.ServerUtils;
import client.MainCtrl;
import client.utils.UndoService;
import commons.DTOs.*;
import javafx.scene.text.Text;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
public class EventPageCtrlValidTests {
    private EventPageCtrl eventPageCtrl;
    @BeforeEach
    public void setUp() {
        ServerUtils mockServer = mock(ServerUtils.class);
        MainCtrl mockMainCtrl = mock(MainCtrl.class);
        UndoService mockUndoService = mock(UndoService.class);
        NodeFactory mockNodeFactory = mock(NodeFactory.class);

        // Create instance of EventPageCtrl with mocked dependencies
        eventPageCtrl = new EventPageCtrl(mockServer, mockMainCtrl, mockUndoService, mockNodeFactory);
    }

    @Test
    public void testInvalidEmail() {
        // Mock dependencies

        // Test invalid email addresses
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@")); // Missing top-level domain
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@example")); // Missing top-level domain extension
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@example.")); // Empty top-level domain
        //assertTrue(eventPageCtrl.invalidEmail("invalidemail@example..com")); // Double dot in domain
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@example..")); // Double dot at end of domain
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@@example.com")); // Double @ symbol
        assertTrue(eventPageCtrl.invalidEmail("invalid email@example.com")); // Space in email
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@example,com")); // Comma instead of dot
        assertTrue(eventPageCtrl.invalidEmail("invalidemail@ex ample.com")); // Space in domain

        // Test valid email addresses
        assertFalse(eventPageCtrl.invalidEmail("validemail@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("valid.email@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("valid_email@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("valid-email@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("validemail123@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("valid.email123@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("valid_email123@example.com"));
        assertFalse(eventPageCtrl.invalidEmail("valid-email123@example.com"));
    }

    @Test
    void isValidAmountTest() {
        // Test valid amounts
        assertEquals(new BigDecimal("100"), eventPageCtrl.isValidAmount("100"));
        assertEquals(new BigDecimal("123.45"), eventPageCtrl.isValidAmount("123.45"));
        assertEquals(new BigDecimal("0.00"), eventPageCtrl.isValidAmount("0.00"));
        assertEquals(new BigDecimal("-50"), eventPageCtrl.isValidAmount("-50"));

        // Test invalid amounts
        assertNull(eventPageCtrl.isValidAmount("abc")); // Non-numeric input
        assertNull(eventPageCtrl.isValidAmount("12.34.56")); // Multiple decimal points
        assertNull(eventPageCtrl.isValidAmount("12a")); // Alphanumeric input
        assertNull(eventPageCtrl.isValidAmount("12..34")); // Double decimal points
        assertNull(eventPageCtrl.isValidAmount("")); // Empty string

        // Test null input
        assertNull(eventPageCtrl.isValidAmount(null));
    }

    @Test
    public void testCheckInput() {
        // Test case 1: All inputs are valid
        assertFalse(eventPageCtrl.checkInput("Expense", "100", "USD", LocalDate.now(), new ParticipantDTO()));

        // Test case 2: Name is null
        assertTrue(eventPageCtrl.checkInput(null, "100", "USD", LocalDate.now(), new ParticipantDTO()));

        // Test case 3: Name is empty
        assertTrue(eventPageCtrl.checkInput("", "100", "USD", LocalDate.now(), new ParticipantDTO()));

        // Test case 4: Author is null
        assertTrue(eventPageCtrl.checkInput("Expense", "100", "USD", LocalDate.now(), null));

        // Test case 5: Transaction amount is null
        assertTrue(eventPageCtrl.checkInput("Expense", null, "USD", LocalDate.now(), new ParticipantDTO()));

        // Test case 6: Transaction amount is empty
        assertTrue(eventPageCtrl.checkInput("Expense", "", "USD", LocalDate.now(), new ParticipantDTO()));

        // Test case 7: Currency is null
        assertTrue(eventPageCtrl.checkInput("Expense", "100", null, LocalDate.now(), new ParticipantDTO()));

        // Test case 8: Date is null
        assertTrue(eventPageCtrl.checkInput("Expense", "100", "USD", null, new ParticipantDTO()));
    }
}

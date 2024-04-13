package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParticipantTest {
   Participant p1;
   Participant p2;
   Event event;

   String firstName = "fname";
   String lastName = "lname";
   String email = "fname@gmail.com";
   String iban = "NL02ABNA0123456789";
   String bic = "Bic1234";

   @BeforeEach
   public void setup() {
      event = new Event("event");
      p1 = new Participant(event,firstName, lastName, email, iban, bic);
      p2 = new Participant(event, firstName, lastName, email, iban, bic);
      p2.id = p1.id;
   }

   @Test
   public void checkConstructor(){
      Event event = new Event("event");
      Participant participant = new Participant(event,firstName, lastName, email, iban, bic);
      assertEquals(event, participant.event);
      assertEquals(firstName, participant.firstName);
      assertEquals(lastName, participant.lastName);
      assertEquals(email, participant.email);
      assertEquals(iban, participant.iban);
      assertEquals(bic, participant.bic);
   }

   @Test
   void testEquals() {
      assertEquals(p1,p2);
   }

   @Test
   void testNotEquals() {
      // Same first and last names but different emails and iban and bic
      String firstName2 = "Jane";
      String lastName2 = "Doe";
      String email2 = "jane_doe2@gmail.com";
      String iban2 = "NL07ABNA0003456789";
      String bic2 = "Bic4321";
      Event event2 = new Event("event2");
      Participant p3 = new Participant(event2, firstName2, lastName2, email2, iban2, bic2);
      assertNotEquals(p1,p3);
   }

   @Test
   void equalsHashCode() {
      assertEquals(p1,p2);
      assertEquals(p1.hashCode(), p2.hashCode());
   }

   @Test
   void testToString() {
      String actual = p1.toString();

      assertTrue(actual.contains(Participant.class.getSimpleName()));
      assertTrue(actual.contains("firstName=" + firstName));
      assertTrue(actual.contains("lastName=" + lastName));
      assertTrue(actual.contains("email=" + email));
      assertTrue(actual.contains("iban=" + iban));
      assertTrue(actual.contains("bic=" + bic));
   }
}
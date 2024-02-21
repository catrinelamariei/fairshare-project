package commons;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParticipantTest {

   @Test
   public void checkConstructor(){
      String firstName = "fname";
      String lastName = "lname";
      String email = "fname@gmail.com";
      String iban = "NL02ABNA0123456789";
      Participant participant = new Participant(firstName, lastName, email, iban);
      assertEquals(firstName, participant.firstName);
      assertEquals(lastName, participant.lastName);
      assertEquals(email, participant.email);
      assertEquals(iban, participant.iban);
   }

   @Test
   void testEquals() {
      String firstName = "Jane";
      String lastName = "Doe";
      String email = "jane_doe@gmail.com";
      String iban = "NL02ABNA0123456789";
      Participant p1 = new Participant(firstName, lastName, email, iban);
      Participant p2 = new Participant(firstName, lastName, email, iban);
      assertEquals(p1,p2);
   }

   @Test
   void testNotEquals() {
      // Same first and last names but different emails and iban
      String firstName = "Jane";
      String lastName = "Doe";
      String email = "jane_doe@gmail.com";
      String iban = "NL02ABNA0123456789";
      Participant p1 = new Participant(firstName, lastName, email, iban);
      String firstName2 = "Jane";
      String lastName2 = "Doe";
      String email2 = "jane_doe2@gmail.com";
      String iban2 = "NL07ABNA0003456789";
      Participant p2 = new Participant(firstName2, lastName2, email2, iban2);
      assertNotEquals(p1,p2);
   }

   @Test
   void equalsHashCode() {
      String firstName = "Jane";
      String lastName = "Doe";
      String email = "jane_doe@gmail.com";
      String iban = "NL02ABNA0123456789";
      Participant p1 = new Participant(firstName, lastName, email, iban);
      Participant p2 = new Participant(firstName, lastName, email, iban);
      assertEquals(p1,p2);
      assertEquals(p1.hashCode(), p2.hashCode());
   }

   @Test
   void testToString() {
      String firstName = "Jane";
      String lastName = "Doe";
      String email = "jane_doe@gmail.com";
      String iban = "NL02ABNA0123456789";
      Participant p = new Participant(firstName, lastName, email, iban);
      String actual = p.toString();

      assertTrue(actual.contains(Participant.class.getSimpleName()));
      assertTrue(actual.contains("firstName=Jane"));
      assertTrue(actual.contains("lastName=Doe"));
      assertTrue(actual.contains("email=jane_doe@gmail.com"));
      assertTrue(actual.contains("iban=NL02ABNA0123456789"));
   }
}
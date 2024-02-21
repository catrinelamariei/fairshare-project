package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Participant {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   public long id;

   public String firstName;
   public String lastName;
   public String email;
   public String iban;

   @SuppressWarnings("unused")
   private Participant() {
      // for object mapper
   }

   public Participant(String firstName, String lastName, String email, String iban) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.iban = iban;
   }

}
package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

   @Override
   public boolean equals(Object obj) {
      return EqualsBuilder.reflectionEquals(this, obj);
   }

   @Override
   public int hashCode() {
      return HashCodeBuilder.reflectionHashCode(this);
   }

   @Override
   public String toString() {
      return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
   }
}
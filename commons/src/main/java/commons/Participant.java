package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

@Entity
public class Participant {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   public UUID id;

   @Column(columnDefinition = "CLOB NOT NULL")
   public String firstName;

   @Column(columnDefinition = "CLOB NOT NULL")
   public String lastName;

   @Column(columnDefinition = "CLOB NOT NULL")
   public String email;

   @Column(columnDefinition = "CLOB NOT NULL")
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

   public UUID getId() {
      return id;
   }
   public String getFirstName() {
      return firstName;
   }
   public String getLastName() {
      return lastName;
   }
   public String getEmail() {
      return email;
   }
   public String getIban(){
      return iban;
   }

   public void setID(UUID id){
      this.id = id;
   }

   public void setFirstName(String fName){
      this.firstName = fName;
   }

   public void setLastName(String lName){
      this.lastName = lName;
   }

   public void setEmail(String email){
      this.email = email;
   }

   public void setIban(String iban){
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
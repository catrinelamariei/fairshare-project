package commons;

import jakarta.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Participant {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   public long id;

   public String firstName;
   public String lastName;
   public String email;
   public String iban;
   @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
   public Set<Transaction> paidTransactions;

   @ManyToMany(mappedBy = "participants")
   public Set<Transaction> participatedTransactions;

   @SuppressWarnings("unused")
   private Participant() {
      // for object mapper
   }

   public Participant(String firstName, String lastName, String email, String iban) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.iban = iban;
      this.paidTransactions = new HashSet<>();
      this.participatedTransactions = new HashSet<>();
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
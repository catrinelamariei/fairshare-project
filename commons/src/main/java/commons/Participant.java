package commons;

import jakarta.persistence.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Set;
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

    @ManyToOne
    public Event event;
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
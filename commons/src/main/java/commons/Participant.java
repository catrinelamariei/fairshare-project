package commons;

import commons.DTOs.ParticipantDTO;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

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

    @Column(columnDefinition = "CLOB NOT NULL")
    public String bic;

    @ManyToOne
    public Event event;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    public Set<Transaction> paidTransactions;

    @ManyToMany(mappedBy = "participants")
    public Set<Transaction> participatedTransactions;

    @SuppressWarnings("unused")
    public Participant() {
      // for object mapper
    }

    public Participant(Event event, String firstName,
                       String lastName, String email, String iban, String bic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.iban = iban;
        this.bic = bic;
        this.paidTransactions = new HashSet<>();
        this.participatedTransactions = new HashSet<>();
        this.event = event;
    }

    public Participant(ParticipantDTO p) {
        this.id = p.id;
        this.firstName = p.firstName;
        this.lastName = p.lastName;
        this.email = p.email;
        this.iban = p.iban;
        this.bic = p.bic;
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
    public String getBic(){
        return bic;
    }
    public Event getEvent() {
        return event;
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

    public void setBic(String bic) {
        this.bic = bic;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Participant)) {
            return false;
        }
        Participant other = (Participant) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(firstName, other.firstName) &&
                Objects.equals(lastName, other.lastName) &&
                Objects.equals(email, other.email) &&
                Objects.equals(iban, other.iban) &&
                Objects.equals(bic, other.bic);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, email, iban, bic);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }


    public void addTransaction(Transaction transaction) {
        participatedTransactions.add(transaction);
    }

    public Set<Transaction> getParticipatedTransactions() {
        return participatedTransactions;
    }

    public Set<Transaction> getPaidTransaction() {
        return paidTransactions;
    }
}
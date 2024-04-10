package commons;

import commons.DTOs.TransactionDTO;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.*;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;
    @Column(columnDefinition = "TIMESTAMP(0)")
    public Date date;
    public String currencyCode;
    @Column(columnDefinition = "NUMERIC(21,2)")
    public BigDecimal amount;
    @ManyToOne
    public Participant author;

    @ManyToOne
    public Event event;

    @ManyToMany()
    public Set<Participant> participants;

    @ManyToMany
    public Set<Tag> tags;

    @Column(nullable = false)
    public String subject;


    @SuppressWarnings("unused")
    public Transaction() {
        // for object mapper (needs to be public/protected)
    }

    public Transaction(Event event, Date date, String currencyCode,
                       BigDecimal amount, Participant author, String subject) {
        this.event = event;
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.tags = new HashSet<>();
        this.author = author;
        this.participants = new HashSet<>();
        this.subject = subject;
    }

    public Transaction(TransactionDTO ts) {
        this.id = ts.id;
        this.date = ts.date;
        this.currencyCode = ts.currencyCode;
        this.amount = ts.amount;
        this.participants = new HashSet<>();
        this.tags = new HashSet<>();
        this.subject = ts.subject;
    }

    public UUID getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Participant getAuthor() {
        return author;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public String getSubject() {
        return subject;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public Event getEvent() {
        return event;
    }

    public void setAuthor(Participant author) {
        this.author = author;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction other = (Transaction) obj;
        return Objects.equals(id, other.id) &&
                Objects.equals(date, other.date) &&
                Objects.equals(currencyCode, other.currencyCode) &&
                Objects.equals(amount, other.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, currencyCode, amount);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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

    @ManyToMany(cascade = CascadeType.DETACH)
    public Set<Participant> participants;

    @ManyToMany(cascade = CascadeType.DETACH)
    public Set<Tag> tags;

    @Column(nullable = false)
    public String subject;


    @SuppressWarnings("unused")
    public Transaction() {
        // for object mapper (needs to be public/protected)
    }

    public Transaction(Event event, Date date,String currencyCode,
                       BigDecimal amount, Participant author, String subject) {
        this.event = event;
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.tags = new HashSet<Tag>();
        this.author = author;
        this.participants = new HashSet<>();
        this.subject = subject;
    }

    /**
     * validates a transaction
     * @param ts transaction to be checked
     * @return true if is valid (not null and all values present and amount is not negative)
     */
    public static boolean validate(Transaction ts) {
        return !(ts == null || ts.id == null || ts.date == null || ts.currencyCode == null ||
            ts.amount == null || ts.currencyCode.isEmpty() ||
            ts.amount.compareTo(BigDecimal.ZERO) < 0 || ts.subject == null);
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
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

    @ManyToMany(cascade = CascadeType.DETACH)
    public Set<Participant> participants;

    @ManyToMany(cascade = CascadeType.DETACH)
    public Set<Tag> tags;



    @SuppressWarnings("unused")
    private Transaction() {
        // for object mapper
    }

    public Transaction(Date date,String currencyCode, BigDecimal amount, Participant author) {
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.tags = new HashSet<Tag>();
        this.author = author;
        this.participants = new HashSet<>();
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
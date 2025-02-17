package commons.DTOs;

import commons.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionDTO {
    public UUID id;
    public UUID eventId;
    public Date date;
    public String currencyCode;
    public BigDecimal amount;
    public ParticipantDTO author;
    public Set<ParticipantDTO> participants;
    public Set<TagDTO> tags;
    public String subject;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.date = transaction.getDate();
        this.currencyCode = transaction.getCurrencyCode();
        this.amount = transaction.getAmount();
        this.author = new ParticipantDTO(transaction.getAuthor());
        this.participants = transaction.getParticipants().stream().map(ParticipantDTO::new)
                .collect(Collectors.toSet());
        this.tags = transaction.getTags().stream().map(TagDTO::new).collect(Collectors.toSet());
        this.eventId = transaction.getEvent().getId();
        this.subject = transaction.getSubject();
    }

    public TransactionDTO(UUID id, UUID eventId, Date date, String currencyCode, BigDecimal amount,
                          ParticipantDTO author, Set<ParticipantDTO> participants, Set<TagDTO> tags,
                          String subject) {
        this.id = id;
        this.eventId = eventId;
        this.date = date;
        this.currencyCode = currencyCode;
        this.amount = amount;
        this.author = author;
        this.participants = participants;
        this.tags = tags;
        this.subject = subject;
    }

    public boolean validate() {
        return !(eventId == null || date == null || currencyCode == null || amount == null ||
            author == null || participants == null || tags == null || subject == null ||
            currencyCode.isEmpty() || subject.isEmpty() || amount.compareTo(BigDecimal.ZERO) <= 0);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public ParticipantDTO getAuthor() {
        return author;
    }

    public void setAuthor(ParticipantDTO author) {
        this.author = author;
    }

    public Set<ParticipantDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<ParticipantDTO> participants) {
        this.participants = participants;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO that = (TransactionDTO) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getEventId(), that.getEventId()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getCurrencyCode(), that.getCurrencyCode()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getAuthor(), that.getAuthor()) &&
                Objects.equals(getParticipants(), that.getParticipants()) &&
                Objects.equals(getTags(), that.getTags()) &&
                Objects.equals(getSubject(), that.getSubject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEventId(), getDate(), getCurrencyCode(),
                getAmount(), getAuthor(), getParticipants(), getTags(), getSubject());
    }
}

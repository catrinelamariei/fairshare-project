package commons.DTOs;

import commons.Participant;

import java.util.Objects;
import java.util.UUID;

public class ParticipantDTO {
    public UUID id;
    public UUID eventId;
    public String firstName;
    public String lastName;
    public String email;
    public String iban;

    public ParticipantDTO() {}

    public ParticipantDTO(Participant participant){
        this.id = participant.getId();
        this.firstName = participant.getFirstName();
        this.lastName = participant.getLastName();
        this.email = participant.getEmail();
        this.iban = participant.getIban();
        this.eventId = participant.getEvent().getId();
    }

    public ParticipantDTO(String firstName, String lastName, String email, String iban) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.iban = iban;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipantDTO that = (ParticipantDTO) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName)
                && Objects.equals(email, that.email) && Objects.equals(iban, that.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, iban);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }
}

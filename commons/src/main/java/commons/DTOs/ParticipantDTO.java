package commons.DTOs;

import commons.Participant;

import java.util.UUID;

public class ParticipantDTO {
   public UUID id;
   public String firstName;
   public String lastName;
   public String email;
   public String iban;

   public ParticipantDTO(Participant participant){
      this.id = participant.getId();
      this.firstName = participant.getFirstName();
      this.lastName = participant.getLastName();
      this.email = participant.getEmail();
      this.iban = participant.getIban();
   }
}

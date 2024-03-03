package server.api;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.ParticipantRepository;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParticipantControllerTest {

   private ParticipantController sut;
   private ParticipantRepository repo;

   @BeforeEach
   public void setup() {
      sut = new ParticipantController(repo);
   }

   @Test
   public void cannotAddNullParticipant() {
      Participant p = null;
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutFirstName() {
      Participant p = new Participant("", "Walker", "jwalker@gmail.com", "NL012345");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutLastName() {
      Participant p = new Participant("Jake", "", "jwalker@gmail.com", "NL012345");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutEmail() {
      Participant p = new Participant("Jake", "Walker", "", "NL012345");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutIban() {
      Participant p = new Participant("Jake", "Walker", "jwalker@gmail.com", "");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

}
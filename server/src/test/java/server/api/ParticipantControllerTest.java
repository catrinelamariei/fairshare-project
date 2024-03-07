package server.api;

import commons.Event;
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
      Event e = new Event("event");
      Participant p = new Participant(e,"", "Walker", "jwalker@gmail.com", "NL012345");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutLastName() {
      Event e = new Event("event");
      Participant p = new Participant(e,"Jake", "", "jwalker@gmail.com", "NL012345");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutEmail() {
        Event event = new Event("event");
      Participant p = new Participant(event,"Jake", "Walker", "", "NL012345");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

   @Test
   public void cannotCreateParticipantWithoutIban() {
      Event event = new Event("event");
      Participant p = new Participant(event,"Jake", "Walker", "jwalker@gmail.com", "");
      var actual = sut.createParticipant(p);
      assertEquals(BAD_REQUEST, actual.getStatusCode());
   }

}
//TODO update or replace tests with mockito


//package server.api;
//
//import commons.Event;
//import commons.Participant;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.ResponseEntity;
//import server.api.dependencies.TestParticipantRepository;
//import server.database.ParticipantRepository;
//
//import java.util.UUID;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.http.HttpStatus.*;
//
//public class ParticipantControllerTest {
//
//   private ParticipantController sut;
//   private ParticipantRepository repo;
//
//   @BeforeEach
//   public void setup() {
//      repo = new TestParticipantRepository();
//      sut = new ParticipantController(repo);
//   }
//
//   @Test
//   public void cannotAddNullParticipant() {
//      Participant p = null;
//      var actual = sut.createParticipant(p);
//      assertEquals(BAD_REQUEST, actual.getStatusCode());
//   }
//
//   @Test
//   public void cannotCreateParticipantWithoutFirstName() {
//      Event e = new Event("event");
//      Participant p = new Participant(e,"", "Walker", "jwalker@gmail.com", "NL012345");
//      var actual = sut.createParticipant(p);
//      assertEquals(BAD_REQUEST, actual.getStatusCode());
//   }
//
//   @Test
//   public void cannotCreateParticipantWithoutLastName() {
//      Event e = new Event("event");
//      Participant p = new Participant(e,"Jake", "", "jwalker@gmail.com", "NL012345");
//      var actual = sut.createParticipant(p);
//      assertEquals(BAD_REQUEST, actual.getStatusCode());
//   }
//
//   @Test
//   public void cannotCreateParticipantWithoutEmail() {
//        Event event = new Event("event");
//      Participant p = new Participant(event,"Jake", "Walker", "", "NL012345");
//      var actual = sut.createParticipant(p);
//      assertEquals(BAD_REQUEST, actual.getStatusCode());
//   }
//
//   @Test
//   public void cannotCreateParticipantWithoutIban() {
//      Event event = new Event("event");
//      Participant p = new Participant(event,"Jake", "Walker", "jwalker@gmail.com", "");
//      var actual = sut.createParticipant(p);
//      assertEquals(BAD_REQUEST, actual.getStatusCode());
//   }
//
//   // TODO: Test for successful post
////   @Test
////   public void successfulPost(){
////        Event e = new Event("event");
////      Participant p = new Participant(e,"Jake", "Walker", "jwalker@gmail.com", "NL294864298");
////      p.id = new UUID(1L, 2L);
////      assertEquals(ResponseEntity.ok().build(), sut.createParticipant(p));
////   }
//
////   @Test
////   public void unsuccessfulPut(){
//////      Event e = new Event("event");
//////      Participant p = new Participant(e,"Jake", "Walker", "jwalker@gmail.com", "NL294864298");
//////      p.id = new UUID(1L, 2L);
//////      Participant p2 = new Participant(e,"John", "Walker", "johnwalker@gmail.com", "NL00098");
//////      p2.id = new UUID(1L, 3L);
//////      assertEquals(NOT_FOUND, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////
//////      p2 = null;
//////      assertEquals(BAD_REQUEST, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////      p2 = new Participant(e,null, "Walker", "johnwalker@gmail.com", "NL00098");
//////      assertEquals(BAD_REQUEST, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////      p2 = new Participant(e,"John", null, "johnwalker@gmail.com", "NL00098");
//////      assertEquals(BAD_REQUEST, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////      p2 = new Participant(e,"John", "Walker", null, "NL00098");
//////      assertEquals(BAD_REQUEST, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////      p2 = new Participant(e,"John", "Walker", "johnwalker@gmail.com", null);
//////      assertEquals(BAD_REQUEST, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////      p2.id = null;
//////      assertEquals(BAD_REQUEST, sut.updateParticipant(p.getId(), p2).getStatusCode());
//////   }
//
//}
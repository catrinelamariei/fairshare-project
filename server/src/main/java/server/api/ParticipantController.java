package server.api;

import commons.DTOs.ParticipantDTO;
import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ParticipantRepository;

import java.util.UUID;

@RestController
@RequestMapping("/participants")
public class ParticipantController {
   private final ParticipantRepository repo;

   public ParticipantController(ParticipantRepository repo){
      this.repo = repo;
   }

   @PostMapping(path = {"" , "/"})
   public ResponseEntity<Participant> createParticipant(@RequestBody Participant participant) {
      if (participant == null || participant.getFirstName() == null || participant.getLastName() == null
              || participant.getFirstName() == "" || participant.getLastName() == "") {
         return ResponseEntity.badRequest().build();
      }

      repo.save(participant);
      return ResponseEntity.ok().build();
   }

   @GetMapping("/{id}")
   public ResponseEntity<ParticipantDTO> getById(@PathVariable("id") UUID id) {
      if (!repo.existsById(id)) {
         return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok(new ParticipantDTO(repo.findById(id).get()));
   }


}

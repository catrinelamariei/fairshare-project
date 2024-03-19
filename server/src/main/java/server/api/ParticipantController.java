package server.api;

import commons.DTOs.ParticipantDTO;
import commons.Participant;
import jakarta.transaction.Transactional;
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

    @Transactional
   @GetMapping("/{id}")
   public ResponseEntity<ParticipantDTO> getById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new ParticipantDTO(repo.findById(id).get()));
    }

    @Transactional
   @PostMapping(path = {"", "/"})
   public ResponseEntity<ParticipantDTO> createParticipant(@RequestBody Participant participant) {
        if (participant == null || participant.getFirstName() == null ||
                participant.getLastName() == null || participant.getFirstName() == ""
                || participant.getLastName() == "" || participant.getEmail() == ""
                || participant.getIban() == ""){
            return ResponseEntity.badRequest().build();
        }

        repo.save(participant);
        ParticipantDTO participantDTO = new ParticipantDTO(participant);
        return ResponseEntity.ok(participantDTO);
    }

    @Transactional
   @PutMapping("/{id}")
   public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable("id") UUID id,
                                                           @RequestBody Participant participant) {
        if(!repo.existsById(id)){
            return ResponseEntity.notFound().build();
        } else if(participant == null || id == null
                || participant.getFirstName() == null || participant.getFirstName() == ""
                || participant.getLastName() == null || participant.getLastName() == ""
                || participant.getEmail() == null || participant.getEmail() == ""
                || participant.getIban() == null || participant.getIban() == "") {
            return ResponseEntity.badRequest().build();
        }

        Participant p = repo.findById(id).get();
        p.firstName = participant.firstName;
        p.lastName = participant.lastName;
        p.email = participant.email;
        p.iban = participant.iban;

        repo.save(p);
        return ResponseEntity.ok(new ParticipantDTO(repo.findById(id).get()));
    }

    @Transactional
   @DeleteMapping("/{id}")
   public ResponseEntity deleteParticipant(@PathVariable ("id") UUID id) {
        if(!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
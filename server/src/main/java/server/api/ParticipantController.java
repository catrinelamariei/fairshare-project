package server.api;

import commons.DTOs.ParticipantDTO;
import commons.Participant;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.DTOtoEntity;
import server.database.ParticipantRepository;

import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;
    private final DTOtoEntity d2e;

    public ParticipantController(ParticipantRepository repo, DTOtoEntity dtoToEntity){
        this.repo = repo;
        this.d2e = dtoToEntity;
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
   public ResponseEntity<ParticipantDTO> createParticipant(
           @RequestBody ParticipantDTO participantDTO) {
        if (participantDTO == null) { //TODO: further validation
            return ResponseEntity.badRequest().build();
        }
        Participant participant = d2e.create(participantDTO);
        return ResponseEntity.ok(new ParticipantDTO(participant));
    }

    @Transactional
   @PutMapping("/{id}")
   public ResponseEntity<ParticipantDTO> updateParticipant(
           @PathVariable("id") UUID id,
           @RequestBody ParticipantDTO participant) {
        if(!repo.existsById(id)){
            return ResponseEntity.notFound().build();
        } else if(participant == null || id == null
                || participant.getFirstName() == null || participant.getFirstName() == ""
                || participant.getLastName() == null || participant.getLastName() == ""
                || participant.getEmail() == null || participant.getEmail() == ""
                || participant.getIban() == null || participant.getIban() == "") {
            return ResponseEntity.badRequest().build();
        }

        Participant p = d2e.update(participant);
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
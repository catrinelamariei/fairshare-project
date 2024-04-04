package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.ParticipantDTO;
import commons.Participant;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.Services.DTOtoEntity;
import server.database.ParticipantRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;
    private final DTOtoEntity d2e;
    private final SimpMessagingTemplate messagingTemplate;

    public ParticipantController(ParticipantRepository repo,
                                 DTOtoEntity dtoToEntity,
                                 SimpMessagingTemplate msgTemplate) {
        this.repo = repo;
        this.d2e = dtoToEntity;
        this.messagingTemplate = msgTemplate;
    }

    @Transactional
   @GetMapping("/{id}")
   public ResponseEntity<ParticipantDTO> getParticipant(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new ParticipantDTO(repo.getReferenceById(id)));
    }

    @Transactional
   @PostMapping(path = {"", "/"})
   public ResponseEntity<ParticipantDTO> createParticipant(
           @RequestBody ParticipantDTO participantDTO) {
        if (participantDTO == null || !participantDTO.validate())
            return ResponseEntity.badRequest().build();
        ParticipantDTO created = new ParticipantDTO(d2e.create(participantDTO));
        EventDTO eventDTO = new EventDTO();
        eventDTO.id = created.eventId;
        eventDTO = new EventDTO(d2e.get(eventDTO));
        if(messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/events", eventDTO);
        }
        return ResponseEntity.ok(created);
    }

    @Transactional
   @PutMapping("/{id}")
   public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable("id") UUID id,
                                                           @RequestBody ParticipantDTO p) {
        if(!repo.existsById(id)) return ResponseEntity.notFound().build();
        if(p == null || !p.validate()) return ResponseEntity.badRequest().build();
        p.id = id;

        ParticipantDTO updated = new ParticipantDTO(d2e.update(p));
        EventDTO eventDTO = new EventDTO();
        eventDTO.id = updated.eventId;
        eventDTO = new EventDTO(d2e.get(eventDTO));
        if(messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/events", eventDTO);
        }
        return ResponseEntity.ok(updated);
    }

    @Transactional
    @PutMapping({"","/"})
    public ResponseEntity<ParticipantDTO> updateParticipant(@RequestBody ParticipantDTO p) {
        return updateParticipant(p.id, p);
    }

    // TODO: manage dependencies
    @Transactional
   @DeleteMapping("/{id}")
   public ResponseEntity deleteParticipant(@PathVariable ("id") UUID id) {
        if(id==null) return ResponseEntity.badRequest().build();
        if(!repo.existsById(id)) return ResponseEntity.notFound().build();
        Optional<Participant> p = repo.findById(id);
        Participant participant = p.get();

        if(messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/events", new EventDTO(participant.event));
        }
        for (Transaction t: participant.getParticipatedTransactions()) {
            t.getParticipants().remove(participant);
        }
        participant.getParticipatedTransactions().clear();
        //participant.getEvent().getParticipants().remove(p);
        repo.delete(participant);
        return ResponseEntity.ok().build();
    }

}
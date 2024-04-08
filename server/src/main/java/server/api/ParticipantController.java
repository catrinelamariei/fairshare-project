package server.api;

import commons.DTOs.*;
import commons.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.Services.DTOtoEntity;
import server.database.ParticipantRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;
    private final DTOtoEntity d2e;
    private final Map<Object, Consumer<ParticipantDTO>> listeners = new ConcurrentHashMap<>();
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
        notifyListeners(created);
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
            messagingTemplate.convertAndSend("/topic/events", new ParticipantDTO(participant)
                    .getEventId());
        }
        for (Transaction t: participant.getParticipatedTransactions()) {
            t.getParticipants().remove(participant);
        }
        participant.getParticipatedTransactions().clear();
        //participant.getEvent().getParticipants().remove(p);
        repo.delete(participant);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<ParticipantDTO>> getParticipantUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<ParticipantDTO>>(5000L, noContent);
        var key = new Object();
        listeners.put(key, t ->{
            res.setResult(ResponseEntity.ok(t));
        });
        res.onCompletion(()->{
            listeners.remove(key);
        });
        return res;
    }


}
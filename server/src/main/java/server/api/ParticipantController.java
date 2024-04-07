package server.api;

import commons.DTOs.ParticipantDTO;
import commons.Participant;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ParticipantController(ParticipantRepository repo, DTOtoEntity dtoToEntity){
        this.repo = repo;
        this.d2e = dtoToEntity;
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
           @RequestBody ParticipantDTO p) {
        if (p == null || !p.validate())
            return ResponseEntity.badRequest().build();
        ParticipantDTO result = new ParticipantDTO(d2e.create(p));

        notifyListeners(result);
        return ResponseEntity.ok(result);
    }

    private void notifyListeners(ParticipantDTO participantDTO) {
        System.out.println("hei?");
        listeners.values().forEach(listener -> {
            listener.accept(participantDTO);
        });
    }

    @Transactional
   @PutMapping("/{id}")
   public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable("id") UUID id,
                                                           @RequestBody ParticipantDTO p) {
        if(!repo.existsById(id)) return ResponseEntity.notFound().build();
        if(p == null || !p.validate()) return ResponseEntity.badRequest().build();
        p.id = id;
        return ResponseEntity.ok(new ParticipantDTO(d2e.update(p)));
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
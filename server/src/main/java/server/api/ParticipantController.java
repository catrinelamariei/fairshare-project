package server.api;

import commons.DTOs.ParticipantDTO;
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
   public ResponseEntity<ParticipantDTO> getParticipant(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new ParticipantDTO(repo.getReferenceById(id)));
    }

    @Transactional
   @PostMapping(path = {"", "/"})
   public ResponseEntity<ParticipantDTO> createParticipant(
           @RequestBody ParticipantDTO participantDTO) {
        if (participantDTO == null || participantDTO.validate())
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ParticipantDTO(d2e.create(participantDTO)));
    }

    @Transactional
   @PutMapping("/{id}")
   public ResponseEntity<ParticipantDTO> updateParticipant(@PathVariable("id") UUID id,
                                                           @RequestBody ParticipantDTO p) {
        if(!repo.existsById(id)) return ResponseEntity.notFound().build();
        if(p == null || p.validate()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new ParticipantDTO(d2e.update(p)));
    }

    // TODO: manage dependencies
    @Transactional
   @DeleteMapping("/{id}")
   public ResponseEntity deleteParticipant(@PathVariable ("id") UUID id) {
        if(!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
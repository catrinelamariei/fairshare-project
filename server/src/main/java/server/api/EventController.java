package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.TagDTO;
import commons.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.DTOtoEntity;
import server.database.EventRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository repo;
    private final DTOtoEntity d2e;
    public EventController(EventRepository repo, DTOtoEntity dtoToEntity) {
        this.repo = repo;
        this.d2e = dtoToEntity;
    }

//    @Transactional
    @PostMapping(path = {"" , "/"})
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO == null || eventDTO.validate()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new EventDTO(d2e.create(eventDTO)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new EventDTO(repo.getReferenceById(id)));
    }

    @Deprecated //because client gets all tags from eventDTO -> different endpoint
    @GetMapping("/{id}/tags")
    public ResponseEntity<Set<TagDTO>> getEventTagsById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Set<Tag> tags = repo.findById(id).get().getTags();
        Set<TagDTO> tagDTOs = new HashSet<>();
        for (Tag tag : tags) {
            tagDTOs.add(new TagDTO(tag));
        }
        return ResponseEntity.ok(tagDTOs);
    }


    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable("id") UUID id,
                                                @RequestBody EventDTO eventDTO) {
        if (id == null || eventDTO == null || eventDTO.validate())
            return ResponseEntity.badRequest().build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        eventDTO.id = id;
        return ResponseEntity.ok(new EventDTO(d2e.update(eventDTO)));
    }

    // TODO: manage dependencies
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteEvent(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

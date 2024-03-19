package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.TagDTO;
import commons.Event;
import commons.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.DTOtoEntity;
import server.database.EventRepository;

import java.util.HashSet;
import java.util.Objects;
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
        if (eventDTO == null || eventDTO.getName() == null
                || Objects.equals(eventDTO.getName(), "")) {
            return ResponseEntity.badRequest().build();
        }
        Event event = d2e.create(eventDTO);

        return ResponseEntity.ok(new EventDTO(event));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new EventDTO(repo.findById(id).get()));
    }

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
    public ResponseEntity<EventDTO> updateById(@PathVariable("id") UUID id,
                                               @RequestBody EventDTO event) {
        if (id==null || event == null || event.getName() == null || event.getName() == "") {
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Event e = repo.findById(id).get();
        e.setName(event.getName());
        repo.save(e);

        return ResponseEntity.ok(new EventDTO(repo.findById(id).get()));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

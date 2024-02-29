package server.api;

import commons.DTOs.EventDTO;
import commons.DTOs.TagDTO;
import commons.Event;
import commons.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository repo;

    public EventController(EventRepository repo) {
        this.repo = repo;
    }



//    @Transactional
    @PostMapping(path = {"" , "/"})
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        if (event == null || event.getName() == null || event.getName() == "") {
            return ResponseEntity.badRequest().build();
        }
        repo.save(event);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new EventDTO(repo.findById(id).get()));
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<Set<TagDTO>> getEventTagsById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Set<Tag> tags = repo.findById(id).get().getTags();
        Set<TagDTO> dtos = new HashSet<>();
        for (Tag tag : tags) {
            dtos.add(new TagDTO(tag));
        }
        return ResponseEntity.ok(dtos);
    }


    @Transactional
    @PutMapping(path = {"" , "/"})
    public ResponseEntity<EventDTO> updateById(@RequestBody Event event) {
        UUID id = event.id;
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event e = repo.findById(id).get();
        e.name = event.name;
        repo.save(e);

        return ResponseEntity.ok(new EventDTO(repo.findById(id).get()));
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

}

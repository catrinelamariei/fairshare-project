package server.api;

import commons.DTOs.*;
import commons.*;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.*;
import server.Authentication.Authenticator;
import server.Services.DTOtoEntity;
import server.database.EventRepository;

import java.util.*;

@RestController
@RequestMapping("/api/event")
public class EventController implements WebMvcConfigurer {
    private final EventRepository repo;
    private final DTOtoEntity d2e;
    private final SimpMessagingTemplate messagingTemplate;

    public EventController(EventRepository repo,
                           DTOtoEntity dtoToEntity,
                           SimpMessagingTemplate msgTemplate) {
        this.repo = repo;
        this.d2e = dtoToEntity;
        this.messagingTemplate = msgTemplate;
    }
    // TODO: move this, also check path url because it doesn't match
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Authenticator()).addPathPatterns("/api/events/");
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Collection<EventDTO>> getAllEvents(){
        return ResponseEntity.ok(repo.findAll().stream().map(EventDTO::new).toList());
    }


//    @Transactional
    @PostMapping(path = {"" , "/"})
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO eventDTO) {
        if (eventDTO == null || !eventDTO.validate()) return ResponseEntity.badRequest().build();
        if (eventDTO.id != null && repo.existsById(eventDTO.id))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        EventDTO created = new EventDTO(d2e.create(eventDTO));
        if(messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/events", created);
        }
        return ResponseEntity.ok(created);
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
        Set<Tag> tags = repo.getReferenceById(id).getTags();
        Set<TagDTO> tagDTOs = new HashSet<>();
        for (Tag tag : tags) {
            tagDTOs.add(new TagDTO(tag));
        }
        return ResponseEntity.ok(tagDTOs);
    }


    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<EventDTO> updateEventName(@PathVariable("id") UUID id,
                                                    @RequestBody EventDTO eventDTO) {
        if (id == null || eventDTO == null  || !eventDTO.validate())
            return ResponseEntity.badRequest().build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        eventDTO.id = id;
        EventDTO updated = new EventDTO(d2e.update(eventDTO));
        if(messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/events", updated);
        }
        return ResponseEntity.ok(updated);
    }

    @Transactional
    @PutMapping
    public ResponseEntity<EventDTO> updateEvent(@RequestBody EventDTO eventDTO) {
        return ResponseEntity.ok(new EventDTO(d2e.set(eventDTO)));
    }

    // TODO: manage dependencies
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteEvent(@PathVariable("id") UUID id) {
        if(id==null) return ResponseEntity.badRequest().build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        Event e = repo.getReferenceById(id);
        repo.delete(e);
        if(messagingTemplate != null) {
            messagingTemplate.convertAndSend("/topic/deletedEvent", new EventDTO(e));
        }
        return ResponseEntity.ok().build();
    }
}

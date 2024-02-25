package server.api;

import commons.Event;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.database.EventRepository;

@RestController
@RequestMapping("/api/event")
public class EventController {
    private final EventRepository repo;

    public EventController(EventRepository repo){
        this.repo = repo;
    }

    @Transactional
    @PostMapping(path = {"" , "/"})
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        if (event == null || event.getName() == null || event.getName() == "") {
            return ResponseEntity.badRequest().build();
        }

        if (repo.existsById(event.getId())) {
            return ResponseEntity.badRequest().build();
        }

        repo.save(event);
        return ResponseEntity.ok().build();
    }
}

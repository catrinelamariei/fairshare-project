package server.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import commons.Participant;
import commons.Tag;
import commons.Transaction;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;
import server.database.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping(path="/data/JSON")
public class JSONController {
    ObjectMapper objectMapper;
    EventRepository eventRepo;
    ParticipantRepository participantRepo;
    TagRepository tagRepo;
    TransactionRepository tsRepo;

    private class DataPackage {
        public List<Event> eventList;
        public List<Participant> participantList;
        public List<Tag> tagList;
        public List<Transaction> tsList;
    }

    private void loadPackage(DataPackage pkg) {
        eventRepo.saveAll(pkg.eventList);
        participantRepo.saveAll(pkg.participantList);
        tagRepo.saveAll(pkg.tagList);
        tsRepo.saveAll(pkg.tsList);
    }

    public JSONController(ObjectMapper objectMapper, EventRepository eventRepo,
                          ParticipantRepository participantRepo, TagRepository tagRepo,
                          TransactionRepository tsRepo) {
        this.objectMapper = objectMapper;
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
        this.tagRepo = tagRepo;
        this.tsRepo = tsRepo;
    }

    /**
     * Gets the data of the ENTIRE database (all events, transactions, tags, participants)
     * @return JSON representation of db
     */
    @GetMapping(path={"", "/"})
    public ResponseEntity<String> getAllJSON() throws JsonProcessingException {
        DataPackage pkg = new DataPackage();
        pkg.eventList = eventRepo.findAll();
        pkg.participantList = participantRepo.findAll();
        pkg.tagList = tagRepo.findAll();
        pkg.tsList = tsRepo.findAll();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(pkg));
    }

    /**
     * Updates/Overwrites the ENTIRE DATABASE with the JSON in body
     * @param json data to be loaded
     * @return pass/fail
     */
    @PutMapping(path={"","/"})
    @Transactional
    public ResponseEntity loadFromJSON(@RequestBody String json) {
        //load DataPKG
        DataPackage pkg;
        try {
            pkg = objectMapper.readValue(json, DataPackage.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        //wipe DB
        eventRepo.deleteAll();
        participantRepo.deleteAll();
        tagRepo.deleteAll();
        tsRepo.deleteAll();

        //load package and return
        loadPackage(pkg);
        return ResponseEntity.ok().build();
    }

    /**
     * returns JSON associated with a single event
     * @param id identifier of desired event
     * @return string containing JSON of event and related entities
     */
    @GetMapping(path="/{id}")
    public ResponseEntity<String> getEventJSON(@PathVariable("id") UUID id)
        throws JsonProcessingException {
        //validate ID (401 - NOT_FOUND)
        if (!eventRepo.existsById(id)) return ResponseEntity.notFound().build();
        Event target = eventRepo.findById(id).get();

        //create DataPackage
        DataPackage pkg = new DataPackage();
        pkg.eventList = List.of(target);
        pkg.participantList = new ArrayList<>(target.getParticipants());
        pkg.tagList = new ArrayList<>(target.getTags());
        // TODO: reimplement below using SQL
        pkg.tsList = pkg.participantList.stream().flatMap(author ->
            author.paidTransactions.stream()).toList();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(pkg));
    }

    /**
     * if event already exit: overwrite said event with json in body
     * if event does not exist: create new event with json in body
     * @param id identifier of event
     * @param json content to be placed
     * @return status code
     */
    @PutMapping(path="/{id}")
    @Transactional
    public ResponseEntity loadEventFromJSON(@PathVariable("id") UUID id, @RequestBody String json) {
        //load and validate DataPKG
        DataPackage pkg;
        try {
            pkg = objectMapper.readValue(json, DataPackage.class);
            if (pkg.eventList.size() != 1) throw new IllegalArgumentException("EVENTCOUNT not 1");
        } catch (JsonProcessingException | IllegalArgumentException e) {
            System.err.println(e);
            return ResponseEntity.badRequest().build();
        }

        //change eventId to mentioned
        Event event = pkg.eventList.getFirst();
        event.id = id;

        //delete eventID and associated values
        if (eventRepo.existsById(id)) {
            eventRepo.deleteById(id); //will delete all associated values through cascading
        }

        loadPackage(pkg);
        return ResponseEntity.ok().build();
    }
}

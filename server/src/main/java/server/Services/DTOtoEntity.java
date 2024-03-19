package server.Services;

import commons.DTOs.EventDTO;
import commons.Event;
import commons.Tag;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;
import server.database.TransactionRepository;

@Service
public class DTOtoEntity {
    public static EventRepository eventRepository;
    public static TransactionRepository transactionRepository;
    public static TagRepository tagRepository;
    public static ParticipantRepository participantRepository;
    public DTOtoEntity(EventRepository eventRepository,
                       TransactionRepository transactionRepository,
                       TagRepository tagRepository,
                       ParticipantRepository participantRepository){
        DTOtoEntity.eventRepository = eventRepository;
        DTOtoEntity.transactionRepository = transactionRepository;
        DTOtoEntity.tagRepository = tagRepository;
        DTOtoEntity.participantRepository = participantRepository;
    }

    public static Event get(EventDTO e){
        return eventRepository.findById(e.id).get();
    }
    public static Event create(EventDTO e){
        Event event = new Event(e.getName());
        event.addTag(new Tag(event, "food", Tag.Color.GREEN));
        event.addTag(new Tag(event, "entrance fees", Tag.Color.BLUE));
        event.addTag(new Tag(event, "travel", Tag.Color.RED));
        eventRepository.save(event);
        return event;
    }

    public static Event update(EventDTO e){
        Event event = eventRepository.findById(e.id).get();
        event.setName(e.getName());
        eventRepository.save(event);
        return event;
    }
    public static boolean delete(EventDTO e){
        eventRepository.deleteById(e.id);
        return true;
    }

}

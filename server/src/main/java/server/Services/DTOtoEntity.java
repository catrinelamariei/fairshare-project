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
    public static EventRepository EVENT_REPOSITORY;
    public static TransactionRepository TRANSACTION_REPOSITORY;
    public static TagRepository TAG_REPOSITORY;
    public static ParticipantRepository PARTICIPANT_REPOSITORY;
    public DTOtoEntity(EventRepository eventRepository, TransactionRepository transactionRepository, TagRepository tagRepository, ParticipantRepository participantRepository){
        EVENT_REPOSITORY = eventRepository;
        TRANSACTION_REPOSITORY = transactionRepository;
        TAG_REPOSITORY = tagRepository;
        PARTICIPANT_REPOSITORY = participantRepository;
    }

   public static Event get(EventDTO e){
        return EVENT_REPOSITORY.findById(e.id).get();
   }
   public static Event create(EventDTO e){
         Event event = new Event(e.getName());
         event.addTag(new Tag(event, "food", Tag.Color.GREEN));
         event.addTag(new Tag(event, "entrance fees", Tag.Color.BLUE));
         event.addTag(new Tag(event, "travel", Tag.Color.RED));
         EVENT_REPOSITORY.save(event);
         return event;
   }

   public static Event update(EventDTO e){
       Event event = EVENT_REPOSITORY.findById(e.id).get();
       event.setName(e.getName());
       EVENT_REPOSITORY.save(event);
       return event;
   }
   public static boolean delete(EventDTO e){
       EVENT_REPOSITORY.deleteById(e.id);
       return true;
   }

}

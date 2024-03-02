package commons.DTOs;

import commons.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.Date;

public class EventDTO {
    public UUID id;
    public String name;
    public Set<TagDTO> tags;
    public Date date;

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.tags = new HashSet<>();
        event.getTags().forEach(tag -> tags.add(new TagDTO(tag)));
        this.date = event.getCreationDate();
    }
}

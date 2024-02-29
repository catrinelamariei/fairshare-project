package commons.DTOs;

import commons.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EventDTO {
    public UUID id;
    public String name;
    public List<TagDTO> tags;

    public EventDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.tags = new ArrayList<>();
        event.getTags().forEach(tag -> tags.add(new TagDTO(tag)));
    }
}

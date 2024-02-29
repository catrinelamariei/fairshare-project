package commons.DTOs;

import commons.Tag;

import java.util.UUID;

public class TagDTO {
    public UUID id;
    public UUID eventId;
    public String name;
    public String color;

    public TagDTO(Tag tag) {
        this.id = tag.id;
        this.eventId = tag.event.id;
        this.name = tag.name;
        this.color = tag.color.toString();
    }
}

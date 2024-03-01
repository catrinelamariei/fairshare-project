package commons.DTOs;

import commons.Tag;

import java.util.UUID;

public class TagDTO {
    public UUID id;
    public UUID eventId;
    public String name;
    public Tag.Color color;

    public TagDTO(Tag tag) {
        this.id = tag.GetId();
        this.eventId = tag.getEvent().getId();
        this.name = tag.getName();
        this.color = tag.getColor();
    }
}

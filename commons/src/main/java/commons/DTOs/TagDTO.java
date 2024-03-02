package commons.DTOs;

import commons.Tag;

import java.util.Objects;
import java.util.UUID;

public class TagDTO {
    public UUID id;
    public UUID eventId;
    public String name;
    public Tag.Color color;

    public TagDTO(Tag tag) {
        this.id = tag.getId();
        this.eventId = tag.getEvent().getId();
        this.name = tag.getName();
        this.color = tag.getColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagDTO tagDTO)) return false;
        return Objects.equals(id, tagDTO.id) &&
            Objects.equals(eventId, tagDTO.eventId) &&
            Objects.equals(name, tagDTO.name) && color == tagDTO.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eventId, name, color);
    }
}

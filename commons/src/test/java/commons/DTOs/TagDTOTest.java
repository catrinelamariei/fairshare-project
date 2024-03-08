package commons.DTOs;

import commons.Event;
import commons.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagDTOTest {
    @Test
    void constructorTest() {
        commons.Tag tag = new commons.Tag(new commons.Event("event"), "tag", commons.Tag.Color.BLUE);
        tag.id = new java.util.UUID(0, 1);
        TagDTO tagDTO = new TagDTO(tag);
        assertEquals(tag.getId(), tagDTO.id);
        assertEquals(tag.getName(), tagDTO.name);
        assertEquals(tag.getColor(), tagDTO.color);
        assertEquals(tag.getEvent().getId(), tagDTO.eventId);
    }

    @Test
    void testEquals() {
        Event event = new Event("event");
        Tag tag = new commons.Tag(event, "tag", commons.Tag.Color.BLUE);
        tag.id = new java.util.UUID(0, 1);
        TagDTO tagDTO = new TagDTO(tag);
        Tag tag2 = new commons.Tag(event, "tag", commons.Tag.Color.BLUE);
        tag2.id = new java.util.UUID(0, 1);
        TagDTO tagDTO2 = new TagDTO(tag2);
        assert tagDTO.equals(tagDTO2);
    }

    @Test
    void testHashCode() {
        Event event = new Event("event");
        Tag tag = new commons.Tag(event, "tag", commons.Tag.Color.BLUE);
        tag.id = new java.util.UUID(0, 1);
        TagDTO tagDTO = new TagDTO(tag);
        Tag tag2 = new commons.Tag(event, "tag", commons.Tag.Color.BLUE);
        tag2.id = new java.util.UUID(0, 1);
        TagDTO tagDTO2 = new TagDTO(tag2);
        assertEquals(tagDTO.hashCode(), tagDTO2.hashCode());
    }
}
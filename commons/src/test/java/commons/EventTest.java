package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event e;
    private List<String> tags;


    @Test
    void checkConstructor() {
        tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e = new Event("name", tags);
        assertEquals("name", e.name);
        assertEquals(tags, e.tags);
    }

    @Test
    void equalsSame() {
        tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e = new Event("name", tags);
        List<String> tags2 = new ArrayList<>();
        tags2.add("tag1");
        tags2.add("tag2");
        Event e2 = new Event("name", tags2);
        assertEquals(e, e2);
    }

    @Test
    void equalsDifferentName() {
        tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e = new Event("name", tags);
        List<String> tags2 = new ArrayList<>();
        tags2.add("tag1");
        tags2.add("tag2");
        Event e2 = new Event("name2", tags2);
        assertNotEquals(e, e2);
    }

    @Test
    void equalsDifferentTags() {
        tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e = new Event("name", tags);
        List<String> tags2 = new ArrayList<>();
        tags2.add("tag1");
        tags2.add("tag3");
        Event e2 = new Event("name", tags2);
        assertNotEquals(e, e2);
    }

    @Test
    void equalsHashCode() {
        tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e = new Event("name", tags);
        List<String> tags2 = new ArrayList<>();
        tags2.add("tag1");
        tags2.add("tag2");
        Event e2 = new Event("name", tags2);
        assertEquals(e.hashCode(), e2.hashCode());
    }

    @Test
    void testToString() {
        tags = new ArrayList<>();
        tags.add("tag1");
        tags.add("tag2");
        e = new Event("name", tags);
        assertNotNull(e.toString());
    }
}
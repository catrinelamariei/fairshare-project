package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

import static commons.Tag.Color.BLUE;
import static commons.Tag.Color.RED;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event e;
    private Tag t;
    private Set<Tag> tags;

    @BeforeEach
    void setup() {
        e = new Event("name");
        t = new Tag(e, "name", BLUE);
        tags = new HashSet<>();
        tags.add(t);
        e.addTag(t);
    }


    @Test
    void checkConstructor() {
        assertEquals("name", e.name);
        assertEquals(tags, e.tags);
    }

    @Test
    void equalsSame() {
        Event e2 = new Event("name");
        Tag t2 = new Tag(e2, "name", BLUE);
        e2.addTag(t2);
        assertEquals(e, e2);
    }

    @Test
    void equalsDifferentName() {
        Event e2 = new Event("name2");
        Tag t2 = new Tag(e2, "name", BLUE);
        e2.addTag(t2);
        assertNotEquals(e, e2);
    }

    @Test
    void equalsDifferentTags() {
        Event e2 = new Event("name");
        Tag t2 = new Tag(e2, "name2", RED);
        e2.addTag(t2);
        assertNotEquals(e, e2);
    }

    @Test
    void equalsHashCode() {
        Event e2 = new Event("name");
        Tag t2 = new Tag(e2, "name", BLUE);
        e2.addTag(t2);
        assertEquals(e.hashCode(), e2.hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(e.toString());
    }

    @Test
    void addTagTrue() {
        Tag t2 = new Tag(e, "name2", RED);
        assertTrue(e.addTag(t2));
    }

    @Test
    void addTestSameTag() {
        boolean temp = e.tags.contains(t);
        assertFalse(e.addTag(t));
    }

    @Test
    void addTestEquivalentTag() {
        Tag t2 = new Tag(e, "name", BLUE);
        assertTrue(e.addTag(t));
        assertFalse(e.addTag(t2));
    }
}
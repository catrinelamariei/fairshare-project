package commons;

import org.junit.jupiter.api.Test;


import java.util.HashSet;
import java.util.Set;

import static commons.Tag.Color.BLUE;
import static commons.Tag.Color.RED;
import static org.junit.jupiter.api.Assertions.*;

class EventTest {



    @Test
    void checkConstructor() {
        Event e = new Event("name");
        Tag t = new Tag(e, "name", BLUE);
        e.addTag(t);
        Set<Tag> tags = new HashSet<>();
        tags.add(t);
        assertEquals("name", e.name);
        assertEquals(tags, e.tags);
    }

//    @Test
//    void equalsSame() {
//        Event e = new Event("name");
//        Tag t = new Tag(e, "name", BLUE);
//        e.addTag(t);
//        Event e2 = new Event("name");
//        Tag t2 = new Tag(e2, "name", BLUE);
//        e2.addTag(t2);
//        assertEquals(e, e2);
//    }

    @Test
    void equalsDifferentName() {
        Event e = new Event("name");
        Tag t = new Tag(e, "name", BLUE);
        e.addTag(t);
        Event e2 = new Event("name2");
        Tag t2 = new Tag(e2, "name", BLUE);
        e2.addTag(t2);
        assertNotEquals(e, e2);
    }

    @Test
    void equalsDifferentTags() {
        Event e = new Event("name");
        Tag t = new Tag(e, "name", BLUE);
        e.addTag(t);
        Event e2 = new Event("name");
        Tag t2 = new Tag(e2, "name2", RED);
        e2.addTag(t2);
        assertNotEquals(e, e2);
    }

    @Test
    void equalsHashCode() {
        Event e = new Event("name");
        Tag t = new Tag(e, "name", BLUE);
        e.addTag(t);
        Event e2 = new Event("name");
        Tag t2 = new Tag(e2, "name", BLUE);
        e2.addTag(t2);
        assertEquals(e.hashCode(), e2.hashCode());
    }

    @Test
    void testToString() {
        Event e = new Event("name");
        Tag t = new Tag(e, "name", BLUE);
        e.addTag(t);
        assertNotNull(e.toString());
    }

    @Test
    void addTagTrue() {
        Event e = new Event("name");
        Tag t = new Tag(e, "name", BLUE);
        assertTrue(e.addTag(t));
    }

//    @Test
//    void addTestSameTag() {
//        Event e = new Event("name");
//        Tag t = new Tag(e, "name", BLUE);
//        assertTrue(e.addTag(t));
//        assertFalse(e.addTag(t));
//    }
//
//    @Test
//    void addTestEquivalentTag() {
//        Event e = new Event("name");
//        Tag t = new Tag(e, "name", BLUE);
//        Tag t2 = new Tag(e, "name", BLUE);
//        assertTrue(e.addTag(t));
//        assertFalse(e.addTag(t2));
//    }
}
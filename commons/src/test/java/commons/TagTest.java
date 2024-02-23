package commons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static commons.Tag.Color.BLUE;
import static commons.Tag.Color.RED;
import static org.junit.jupiter.api.Assertions.*;

class TagTest {

    private Event e;
    private Tag t;

    @BeforeEach
    void setup() {
        e = new Event("name");
        t = new Tag(e, "name", BLUE);
    }

    @Test
    void checkConstructor() {
        assertEquals(e, t.event);
        assertEquals("name", t.name);
        assertEquals(BLUE, t.color);
    }

    @Test
    void equalsSame() {
        Tag t2 = new Tag(e, "name", BLUE);
        assertEquals(t, t2);
    }

    @Test
    void equalsDifferentEvent() {
        Event e2 = new Event("name2");
        Tag t2 = new Tag(e2, "name", BLUE);
        assertNotEquals(t, t2);
    }

    @Test
    void equalsDifferentName() {
        Tag t2 = new Tag(e, "name2", BLUE);
        assertNotEquals(t, t2);
    }

    @Test
    void equalsDifferentColor() {
        Tag t2 = new Tag(e, "name", RED);
        assertNotEquals(t, t2);
    }

    @Test
    void equalsHashCode() {
        Tag t2 = new Tag(e, "name", BLUE);
        assertEquals(t.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        assertNotNull(t.toString());
    }

}
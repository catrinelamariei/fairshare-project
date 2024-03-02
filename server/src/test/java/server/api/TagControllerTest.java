package server.api;

import commons.Event;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TagControllerTest {

    private TestTagRepository repo;
    private TagController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTagRepository();
        sut = new TagController(repo);
    }

    @Test
    public void cannotAddNullTag() {
        ResponseEntity<Tag> actual = sut.add(getTag(null));
        assertEquals(BAD_REQUEST, actual.getStatusCode());
    }

    @Test
    public void databaseisUsed() {
        sut.add(getTag("tag_name"));
        assertTrue(repo.calledMethods.contains("save"));
    }

    private static Tag getTag(String name) {
        return new Tag(new Event("event_name"),  name, Tag.Color.RED);
    }


}
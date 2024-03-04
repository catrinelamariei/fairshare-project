package server.api;

import commons.DTOs.TagDTO;
import commons.Event;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.dependencies.TestTagRepository;

import java.util.UUID;

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
    public void postSuccessful() {
        Tag t = getTag("tag_name");
        assertEquals(ResponseEntity.ok().build(), sut.add(t));
        assertEquals(t, repo.tags.getLast());
        assertTrue(repo.calledMethods.contains("save"));
    }

    @Test
    public void postUnsuccesful() {
        assertEquals(BAD_REQUEST, sut.add(null).getStatusCode());
        Tag t = getTag(null);
        assertEquals(BAD_REQUEST, sut.add(t).getStatusCode());
        t = new Tag(null, "name", Tag.Color.RED);
        assertEquals(BAD_REQUEST, sut.add(t).getStatusCode());
        t = new Tag(new Event(""), "name", Tag.Color.RED);
        assertEquals(BAD_REQUEST, sut.add(t).getStatusCode());
        t = new Tag(new Event("event_name"), null, Tag.Color.RED);
        assertEquals(BAD_REQUEST, sut.add(t).getStatusCode());
        t = new Tag(new Event("event_name"), "name", null);
        assertEquals(BAD_REQUEST, sut.add(t).getStatusCode());
    }


    @Test
    public void getSuccessful() {
        Tag t = getTag("tag_name");
        sut.add(t);
        assertEquals(ResponseEntity.ok(new TagDTO(t)), sut.getById(t.getId()));
        assertTrue(repo.calledMethods.contains("getById"));
    }

    @Test
    public void getUnsuccessful() {
        Tag t = getTag("tag_name");
        assertEquals(BAD_REQUEST, sut.getById(t.getId()).getStatusCode());
    }

    // TODO: repo.calledMethods
    @Test
    public void putSucessful() {
        Tag t = getTag("tag_name");
        sut.add(t);
        sut.updateById(t.getId(), getTag("new_name"));
        t.setName("new_name");
        assertEquals(ResponseEntity.ok(new TagDTO(t)), sut.getById(t.getId()));
    }

    @Test
    public void putUnsuccessful() {
        Tag t = getTag("tag_name");
        Tag t2 = getTag("new_name");
        assertEquals(BAD_REQUEST, sut.updateById(t.getId(), t2).getStatusCode());
        t2 = getTag(null);
        sut.add(t);
        assertEquals(BAD_REQUEST, sut.updateById(t.getId(), t2).getStatusCode());
        t2 = getTag("");
        assertEquals(BAD_REQUEST, sut.updateById(t.getId(), t2).getStatusCode());
        t2 = new Tag(null, "new_name", Tag.Color.RED);
        assertEquals(BAD_REQUEST, sut.updateById(t.getId(), t2).getStatusCode());
        t2 = new Tag(new Event(null), "new_name", Tag.Color.RED);
        assertEquals(BAD_REQUEST, sut.updateById(t.getId(), t2).getStatusCode());
        t2 = new Tag(new Event(""), "new_name", Tag.Color.RED);
        assertEquals(BAD_REQUEST, sut.updateById(t.getId(), t2).getStatusCode());
    }

//    @Test
//    public void deleteSuccessful() {
//        Tag t = getTag("tag_name");
//        sut.add(t);
//        assertEquals(ResponseEntity.ok().build(), sut.deleteById(t.getId()));
//        assertTrue(repo.tags.isEmpty());
//        assertTrue(repo.calledMethods.contains("deleteById"));
//    }


    // helper methods:

    private static Tag getTag(String name) {
        Tag t = new Tag(new Event("event_name"),  name, Tag.Color.RED);
        t.id = new UUID(0L, 0L);
        return t;
    }



}
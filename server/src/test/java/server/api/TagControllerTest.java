package server.api;

import commons.DTOs.TagDTO;
import commons.Event;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.Services.DTOtoEntity;
import server.database.TagRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import static org.mockito.Mockito.*;

public class TagControllerTest {
    private TagController controller;
    private TagRepository repo = mock(TagRepository.class);
    private DTOtoEntity d2e = mock(DTOtoEntity.class);
    private TagDTO tagDTO;
    private Tag tag;

    @BeforeEach
    public void setUp() {
        controller = new TagController(repo,d2e);
        Event event = new Event("event");
        event.id = UUID.randomUUID();
        tag = new Tag(event, "tag", Tag.Color.GREEN);
        tag.id = UUID.randomUUID();
        tagDTO = new TagDTO(tag);
    }

    @Test
    public void getTag() {
        when(repo.existsById(tag.id)).thenReturn(true);
        when(repo.getReferenceById(tag.id)).thenReturn(tag);
        ResponseEntity<TagDTO> response = controller.getTag(tag.id);
        assertEquals(tagDTO, response.getBody());
    }

    @Test
    public void getTagNotFound() {
        when(repo.existsById(tag.id)).thenReturn(false);
        ResponseEntity<TagDTO> response = controller.getTag(tag.id);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void createTag() {
        tagDTO.id = null;
        when(d2e.create(tagDTO)).thenReturn(tag);
        ResponseEntity<TagDTO> response = controller.createTag(tagDTO);
        tagDTO.id = tag.id;
        assertEquals(tagDTO, response.getBody());
    }

    @Test
    public void createTagBadRequest() {
        tagDTO.id = null;
        tagDTO.name = null;
        ResponseEntity<TagDTO> response = controller.createTag(tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        tagDTO.name = "";
        ResponseEntity<TagDTO> response2 = controller.createTag(tagDTO);
        assertEquals(BAD_REQUEST, response2.getStatusCode());

        tagDTO.name = "tag";
        tagDTO.color = null;
        response = controller.createTag(tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        tagDTO.color = Tag.Color.GREEN;
        tagDTO.eventId = null;
        response = controller.createTag(tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        response = controller.createTag(null);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateTag() {
        when(repo.existsById(tag.id)).thenReturn(true);
        when(d2e.update(tagDTO)).thenReturn(tag);
        ResponseEntity<TagDTO> response = controller.updateTag(tag.id, tagDTO);
        assertEquals(tagDTO, response.getBody());
    }

    @Test
    public void updateTagBadRequest() {
        when(repo.existsById(tag.id)).thenReturn(true);

        tagDTO.name = null;
        ResponseEntity<TagDTO> response = controller.updateTag(tag.id, tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        tagDTO.name = "";
        response = controller.updateTag(tag.id, tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        tagDTO.name = "tag";
        tagDTO.color = null;
        response = controller.updateTag(tag.id, tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        tagDTO.color = Tag.Color.GREEN;
        tagDTO.eventId = null;
        response = controller.updateTag(tag.id, tagDTO);
        assertEquals(BAD_REQUEST, response.getStatusCode());

        response = controller.updateTag(tag.id, null);
        assertEquals(BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateTagNotFound() {
        when(repo.existsById(tag.id)).thenReturn(false);
        ResponseEntity<TagDTO> response = controller.updateTag(tag.id, tagDTO);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteTag() {
        when(repo.existsById(tag.id)).thenReturn(true);

        //todo fix name of method
        ResponseEntity response = controller.updateTag(tag.id);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void deleteTagNotFound() {
        when(repo.existsById(tag.id)).thenReturn(false);
        ResponseEntity response = controller.updateTag(tag.id);
        assertEquals(NOT_FOUND, response.getStatusCode());
    }


}
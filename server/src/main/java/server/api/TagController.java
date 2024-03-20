package server.api;

import commons.DTOs.TagDTO;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.Services.DTOtoEntity;
import server.database.TagRepository;

import java.util.UUID;


@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository repo;
    private final DTOtoEntity d2e;

    public TagController(TagRepository repo, DTOtoEntity dtoToEntity){
        this.repo = repo;
        this.d2e = dtoToEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getTag(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new TagDTO(repo.getReferenceById(id)));
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO == null || !tagDTO.validate()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(new TagDTO(d2e.create(tagDTO)));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateTag(@PathVariable("id") UUID id, @RequestBody TagDTO tag) {
        if (tag == null || !tag.validate()) return ResponseEntity.badRequest().build();
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        tag.id = id;
        return ResponseEntity.ok(new TagDTO(d2e.update(tag)));
    }

    // TODO: manage dependencies
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity updateTag(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}

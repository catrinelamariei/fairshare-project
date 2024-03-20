package server.api;

import commons.DTOs.TagDTO;
import commons.Tag;
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
    public ResponseEntity<TagDTO> getById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new TagDTO(repo.findById(id).get()));
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO == null) return ResponseEntity.badRequest().build(); // TODO: better validation
        Tag tag = d2e.create(tagDTO);
        return ResponseEntity.ok(new TagDTO(tag));
    }


    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<TagDTO> updateById(@PathVariable("id") UUID id,@RequestBody Tag tag) {
        if (tag==null ||
            tag.getName() == null ||
            tag.getName().isEmpty() ||
            tag.getColor()==null ||
            tag.getEvent()==null ||
            tag.getEvent().getName()==null ||
            tag.getEvent().getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        Tag t = repo.findById(id).get();
        t.setName(tag.getName());
        repo.save(t);

        return ResponseEntity.ok(new TagDTO(repo.findById(id).get()));
    }

//    @Transactional
//    @DeleteMapping("/{id}")
//    public ResponseEntity deleteById(@PathVariable("id") UUID id) {
//        if (!repo.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        Tag t = repo.findById(id).get();
//        repo.deleteById(id);
//        Event e = t.getEvent();
//        if (!e.deleteTag(t)) {
//            return ResponseEntity.badRequest().build();
//        }
//        return ResponseEntity.ok().build();
//    }




}

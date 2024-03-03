package server.api;

import commons.DTOs.TagDTO;
import commons.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.TagRepository;

import java.util.UUID;


@RestController
@RequestMapping("/api/tags")
public class TagController {


    private final TagRepository repo;

    public TagController(TagRepository repo){
        this.repo = repo;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagDTO> getById(@PathVariable("id") UUID id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new TagDTO(repo.findById(id).get()));
    }

    @PostMapping(path = {"" , "/"})
    public ResponseEntity<Tag> add(@RequestBody Tag tag) {
        if (tag == null ||
            tag.getName() == null ||
            tag.getName().isEmpty() ||
            tag.getColor()==null ||
            tag.getEvent()==null ||
            tag.getEvent().getName() == null ||
            tag.getEvent().getName().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        repo.save(tag);
        return ResponseEntity.ok().build();
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
            return ResponseEntity.badRequest().build();
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
//            return ResponseEntity.badRequest().build();
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

package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping(path = "/reach/")
    public ResponseEntity testReach() {
        return ResponseEntity.ok().build();
    }
}

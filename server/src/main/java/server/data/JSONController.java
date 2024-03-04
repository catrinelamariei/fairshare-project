package server.data;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(path="/data")
public class JSONController {
    @RequestMapping(path={"", "/"})
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().build();
    }
}

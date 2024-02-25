package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class PrivCheckController {

    private static final String code = "abc"; // need to generate random

    @PostMapping("/privCheck")
    @ResponseBody
    public String checkPassword(@RequestBody String password) {
        if (password.equals(code)) {
            return "Token"; // need to return auth token
        } else {
            return "No Token"; // error code maybe?
        }
    }
}

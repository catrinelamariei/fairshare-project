package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class PrivCheckController {

    private static String code = "abc"; // need to generate random

    public PrivCheckController(){
        super();
        this.generatePassword();
    }

    @PostMapping("/privCheck")
    @ResponseBody
    public String checkPassword(@RequestBody String password) {
        if (password.equals(code)) {
            return "Token"; // need to return auth token
        } else {
            return "No Token"; // error code maybe?
        }
    }

    @GetMapping("/privCheck")
    @ResponseBody
    public String generatePassword(){
        code = CodeGenerator.generateRandomString(6);
        System.out.println("Code: " + code);
        return "Code generated";
    }
}

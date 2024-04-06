package server.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrivCheckController {

    private static String code; // need to generate random
    private JwtTokenService jwtTokenService;
    private final TokenBucket tokenBucket = new TokenBucket(2, 1000);

    public PrivCheckController(){
        super();
        this.generatePassword();
    }

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/admin")
    @ResponseBody
    public ResponseEntity<String> checkPassword(@RequestBody String password) {
        if (!tokenBucket.tryConsume()) {
            return new ResponseEntity<>("429 Too Many Requests", HttpStatus.OK);
        }

        if (password.equals(code)) {
            // Instantiate JwtTokenService
            JwtTokenService jwtTokenService = new JwtTokenService();

            // Generate JWT token
            String ipAddress = request.getRemoteAddr();
            return new ResponseEntity<>(JwtTokenService.generateToken(ipAddress), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("401 Invalid password", HttpStatus.OK);
        }
    }

    @GetMapping("/admin")
    @ResponseBody
    public ResponseEntity<String> generatePassword(){
        if (!tokenBucket.tryConsume()) {
            return new ResponseEntity<>("429 Too Many Requests", HttpStatus.OK);
        }

        code = CodeGenerator.generateRandomString(6);
        System.out.println("Code: " + code);
        return new ResponseEntity<>("200 Code generated", HttpStatus.OK);
    }
}

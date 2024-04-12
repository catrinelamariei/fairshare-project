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
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }

        if (password.equals(code)) {
            // Instantiate JwtTokenService
            JwtTokenService jwtTokenService = new JwtTokenService();

            // Generate JWT token
            String ipAddress = request.getRemoteAddr();
            return new ResponseEntity<>(JwtTokenService.generateToken(ipAddress), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/admin")
    @ResponseBody
    public ResponseEntity generatePassword(){
        if (!tokenBucket.tryConsume()) {
            return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
        }

        code = CodeGenerator.generateRandomString(6);
        System.out.println("Code: " + code);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

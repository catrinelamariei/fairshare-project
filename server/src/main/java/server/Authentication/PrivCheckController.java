package server.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import server.Authentication.CodeGenerator;
import server.Authentication.JwtTokenService;
import server.Authentication.TokenBucket;

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
    public String checkPassword(@RequestBody String password) {
        if (!tokenBucket.tryConsume()) {
            return "Too many requests. Please try again later.";
        }

        if (password.equals(code)) {
            // Instantiate JwtTokenService
            JwtTokenService jwtTokenService = new JwtTokenService();

            // Generate JWT token
            String ipAddress = request.getRemoteAddr();
            return JwtTokenService.generateToken(ipAddress);
        } else {
            return "Invalid password"; // Return appropriate error message
        }
    }

    @GetMapping("/admin")
    @ResponseBody
    public String generatePassword(){
        if (!tokenBucket.tryConsume()) {
            return "Too many requests. Please try again later.";
        }

        code = CodeGenerator.generateRandomString(6);
        System.out.println("Code: " + code);
        return "Code generated";
    }
}

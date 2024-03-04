package server.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
@RestController
public class PrivCheckController {

    private static String code; // need to generate random
    private JwtTokenService jwtTokenService;

    public PrivCheckController(){
        super();
        this.generatePassword();
    }


    @PostMapping("/admin")
    @ResponseBody
    public String checkPassword(@RequestBody String password, HttpServletRequest request) {
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
        code = CodeGenerator.generateRandomString(6);
        System.out.println("Code: " + code);
        return "Code generated";
    }
}

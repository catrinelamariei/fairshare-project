package server;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
@RestController
public class JsonData {

    private static String code; // need to generate random
    private JwtTokenService jwtTokenService;

    @GetMapping("/data")
    @ResponseBody
    public String checkPassword(HttpServletRequest request, @RequestHeader("Authorization") String token) {
        String value = token.replace("Bearer ", "");
        JwtTokenService jwtTokenService = new JwtTokenService();
        if(jwtTokenService.isValidToken(value,request.getRemoteAddr())){
            return "YAY";
        }
        else {
            return token;
        }
    }
}

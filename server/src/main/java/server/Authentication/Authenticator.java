package server.Authentication;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class Authenticator implements HandlerInterceptor {

    private static String code; // need to generate random
    private JwtTokenService jwtTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        String token = request.getHeader("Authorization");
        JwtTokenService jwtTokenService = new JwtTokenService();

        //validate token
        if(token != null && jwtTokenService.
                isValidToken(token.replace("Bearer ", ""),request.getRemoteAddr())){
            return true; //continue request
        }
        else {
            response.setStatus(401); //unauthorized request
            return false; //discontinue request (END)
        }
    }
}

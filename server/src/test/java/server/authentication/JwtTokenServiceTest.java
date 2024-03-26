package server.authentication;

import org.junit.jupiter.api.Test;
import server.Authentication.JwtTokenService;

import static org.junit.jupiter.api.Assertions.*;


public class JwtTokenServiceTest {

    @Test
    public void testGenerateToken() {
        String ip = "192.168.8.1";
        JwtTokenService jwtTokenService = new JwtTokenService();

        String token = jwtTokenService.generateToken(ip);

        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    public void testIsValidTokenWithValidToken() {
        String ip = "192.168.8.1";
        JwtTokenService jwtTokenService = new JwtTokenService();
        String token = jwtTokenService.generateToken(ip);

        boolean isValid = jwtTokenService.isValidToken(token, ip);

        assertTrue(isValid);
    }

    @Test
    public void testIsValidTokenWithInvalidToken() {
        String ip = "192.168.1.1";
        JwtTokenService jwtTokenService = new JwtTokenService();
        String token = "invalidToken";

        boolean isValid = jwtTokenService.isValidToken(token, ip);
        
        assertFalse(isValid);
    }
}

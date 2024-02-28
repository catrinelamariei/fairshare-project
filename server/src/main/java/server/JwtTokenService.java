package server;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
@Service
public class JwtTokenService {

    private static final String secretKey = "9gC3mOzg/wLoijVE9QUI7jS+4zog1zM1Et/w7C1MqvRRHi/MWgJf0SGBW0nD1x9b";

        public static String generateToken(String ip) {
            return Jwts.builder()
                    .setSubject(ip)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }

        public boolean isValidUsername(String token, String ip) {
            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();

                String tokenIp = claims.getSubject();
                return ip.equals(tokenIp);
            } catch (Exception e) {
                return false; // Token validation failed
            }
        }
    }

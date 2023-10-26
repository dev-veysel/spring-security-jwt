package com.hacker.security.jwt;

import com.hacker.exception.message.ErrorMessage;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private final SecretKey jwtSecret = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

    @Value("${hackers_security.app.jwtExpirationMs}")// 24 Stunden
    private Long jwtExpirationMs;

    // Generate JWT token
    public String generateJwtToken(UserDetails userDetails){
        return Jwts.builder() //JWT Token Informationen!
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs)) // JETZT + 24 Stunden
                .signWith(jwtSecret)
                .compact(); //<-- sammelt alles zusammen
    }

    // JWT Token -> email extrahieren (damit wir die unique Email des Users erhalten können)
    public String getEmailFromToken(String token){
        return Jwts.parser()
                .setSigningKey(jwtSecret) //SecretKey nur bei uns, verschlüsseltes beim Client!
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT validate
    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException |
                 SignatureException | IllegalArgumentException e) {
            logger.error(String.format(
                    ErrorMessage.JWTTOKEN_ERROR_MESSAGE, e.getMessage()));
        }
        return false;
    }
}
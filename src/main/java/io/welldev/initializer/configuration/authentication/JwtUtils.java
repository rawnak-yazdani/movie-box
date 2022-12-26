package io.welldev.initializer.configuration.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.welldev.model.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${TOKEN_SECRET_KEY}")
    private String jwtSecret;

    @Value("${TOKEN_EXPIRE_TIME}")
    private int jwtExpirationMs;

//    public String generateJwtToken(UserDetailsImpl userPrincipal) {
//        return generateTokenFromUsername(userPrincipal.getUsername());
//    }

    public String generateAccessTokenFromUsername(String username, Date dateIat, Date dateExp) {
        return Jwts.builder().setSubject(username).setIssuedAt(dateIat)
                .setExpiration(dateExp)
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public String generateRefreshTokenFromUsername(String username) {
        return Jwts.builder().setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((System.currentTimeMillis() + jwtExpirationMs * 30 * 24 * 60 * 60 * 1000)))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
//            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                    .build()
                    /**
                     * parseClaimsJws(token) method verifies the token
                     */
                    .parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public boolean isExpired(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(authToken);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            return false;
        }

    }

}
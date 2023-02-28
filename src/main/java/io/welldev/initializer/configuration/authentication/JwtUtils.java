package io.welldev.initializer.configuration.authentication;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.welldev.model.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Collection;
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

    public String generateTokenFromUsername(String username, Collection<? extends GrantedAuthority> authorities,
                                            Date dateIat, Date dateExp) {
        return Jwts.builder()
                .setSubject(username)
                .claim(Constants.AppStrings.AUTHORITIES, authorities)
                .setIssuedAt(dateIat)
                .setExpiration(dateExp)
                .signWith(Keys.hmacShaKeyFor(System.getenv(Constants.AppStrings.TOKEN_SECRET_KEY).getBytes()))
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
            throw e;
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

    public Date getTokenExpireDate(int lifetimeOfToken) {
        Calendar calendarAccessToken = Calendar.getInstance();
        calendarAccessToken.add(Calendar.MINUTE, lifetimeOfToken);
        return calendarAccessToken.getTime();
    }
}
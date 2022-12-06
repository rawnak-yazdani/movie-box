package io.welldev.initializer.configuration.userauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.welldev.model.entity.CinephileCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.remoting.jaxws.JaxWsPortClientInterceptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class AppUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            CinephileCredentials appUserDetails = new ObjectMapper()
                    .readValue(request.getInputStream(), CinephileCredentials.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    appUserDetails.getUsername(),
                    appUserDetails.getPassword()
            );
            authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String secretKey = "jumanjiScaresTheSoulOUTTAME";
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("name", String.valueOf(authResult.getPrincipal()))
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(2L)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        response.addHeader("UserAuthorization", "Bearer " + token);

        super.successfulAuthentication(request, response, chain, authResult);
    }
}

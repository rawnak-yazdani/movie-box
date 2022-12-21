package io.welldev.initializer.configuration.userauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import io.welldev.model.entity.AppUser;
import io.welldev.model.service.BlackListingService;
import lombok.RequiredArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@RequiredArgsConstructor
public class AppUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    /**
     * This method will be called first during login
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            AppUser appUserDetails = new ObjectMapper()
                    .readValue(request.getInputStream(), AppUser.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    appUserDetails.getUsername(),
                    appUserDetails.getPassword()
            );
            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method will be called third during login
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        int lifeTime = Integer.parseInt(System.getenv("TOKEN_EXPIRE_TIME"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, lifeTime);
        Date dateIat = new Date();
        Date dateExp = calendar.getTime();

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(dateIat)
                .setExpiration(dateExp)
                .signWith(Keys.hmacShaKeyFor(System.getenv("TOKEN_SECRET_KEY").getBytes()))
                .compact();

        JSONObject jsonObjectOfResponseBody = new JSONObject();

        try {
            jsonObjectOfResponseBody.put("issued at", dateIat.toString());
            jsonObjectOfResponseBody.put("expire at", dateExp.toString());
            jsonObjectOfResponseBody.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        response.getWriter().write(jsonObjectOfResponseBody.toString());
//        super.successfulAuthentication(request, response, chain, authResult);
    }
}

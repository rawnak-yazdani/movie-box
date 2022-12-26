package io.welldev.initializer.configuration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.entity.AppUser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Component
public class AppUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AppUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                                      JwtUtils jwtUtils,
                                                      AuthenticationFailureHandler authenticationFailureHandler) {
        super.setAuthenticationManager(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    /**
     * This method will be called first during login [called form doFilter() of AbstractAuthenticationProcessingFilter]
     * UsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter
     * AbstractAuthenticationProcessingFilter extends GenericFilterBean
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
        int lifeTime = Integer.parseInt(System.getenv(AppStrings.TOKEN_EXPIRE_TIME));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, lifeTime);
        Date dateIat = new Date();
        Date dateExp = calendar.getTime();

        String token = jwtUtils.generateAccessTokenFromUsername(authResult.getName());

        JSONObject jsonObjectOfResponseBody = new JSONObject();

        try {
            jsonObjectOfResponseBody.put(AppStrings.ISSUED_AT, dateIat.toString());
            jsonObjectOfResponseBody.put(AppStrings.EXPIRE_AT, dateExp.toString());
//            jsonObjectOfResponseBody.put(AppStrings.TOKEN, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Cookie cookieToken = new Cookie(AppStrings.TOKEN, token);
        cookieToken.setHttpOnly(true);
        cookieToken.setSecure(true);

        response.addCookie(cookieToken);
        response.getWriter().write(jsonObjectOfResponseBody.toString());
//        super.successfulAuthentication(request, response, chain, authResult);
    }


}

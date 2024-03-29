package io.welldev.initializer.configuration.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.welldev.initializer.configuration.authorization.JwtTokenVerifier;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.entity.AppUser;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
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

    @Value("${TOKEN_EXPIRE_TIME}")
    private String jwtExpireTime;

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
        int lifeTimeOfToken = Integer.parseInt(jwtExpireTime);
        Date dateExpAccessToken = jwtUtils.getTokenExpireDate(lifeTimeOfToken);
        Date dateExpRefreshToken = jwtUtils.getTokenExpireDate(lifeTimeOfToken * 84);

        Date dateIssuedAt = new Date();

        String accessToken = jwtUtils.generateTokenFromUsername(authResult.getName(), authResult.getAuthorities(),
                dateIssuedAt, dateExpAccessToken);
        String refreshToken = jwtUtils.generateTokenFromUsername(authResult.getName(), authResult.getAuthorities(),
                dateIssuedAt, dateExpRefreshToken);
        String username = authResult.getName();
        String role = "";

        for (GrantedAuthority s : authResult.getAuthorities()) {
            if (s.toString().contains("ROLE")) {
                if (s.toString().matches("ROLE_ADMIN"))
                    role = "admin";
                else
                    role = "user";
                break;
            }
        }

        Cookie cookieOfAccessToken = JwtTokenVerifier.writeJwtResponse(
                response, username, role, dateExpAccessToken, dateIssuedAt, accessToken
        );
//        cookieOfAccessToken.setSecure(true);

        Cookie cookieOfRefreshToken = new Cookie(AppStrings.REFRESH_TOKEN, refreshToken);
        cookieOfRefreshToken.setHttpOnly(true);
//        cookieOfRefreshToken.setSecure(true);

        response.addCookie(cookieOfAccessToken);
        response.addCookie(cookieOfRefreshToken);
//        super.successfulAuthentication(request, response, chain, authResult);
    }


}

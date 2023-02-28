package io.welldev.initializer.configuration.authorization;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.welldev.initializer.configuration.authentication.JwtUtils;
import io.welldev.model.constants.Constants;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.service.BlackListingService;
import lombok.RequiredArgsConstructor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    private final BlackListingService blackListingService;

    private final JwtUtils jwtUtils;

    @Value("${TOKEN_EXPIRE_TIME}")
    private String jwtExpireTime;

    /**
     * This method will be called when user hits an API which requires authorization
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
//        String token = request.getHeader(AppStrings.AUTHORIZATION);
        Optional<String> stringRawCookie = Optional.ofNullable(request.getHeader(AppStrings.COOKIE));
        String[] stringCookie;
        String userPublicURI = "POST/movie-box/users";
        String moviePublicURI = "GET/movie-box/movies";
        String tokenPublicURI = "GET/movie-box/token";
        String requestedURI = request.getMethod() + request.getRequestURI();
        boolean isPublic = (requestedURI.equals(userPublicURI))
                || (requestedURI.equals(moviePublicURI) || (requestedURI.equals(tokenPublicURI))
        );
        System.out.println(requestedURI + ' ' + isPublic);
        if (stringRawCookie.isPresent() && !isPublic) {
            stringCookie = request.getHeader(AppStrings.COOKIE).split(";");
        } else {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = stringCookie[0].replace("ACCESS_TOKEN=", "");
        String refreshToken = stringCookie[1].replace("REFRESH_TOKEN=", "");
//        System.out.println(requestedURI + isPublic);

        try {

            if (!jwtUtils.validateJwtToken(accessToken)) {

            } else {
                String blackListedToken = blackListingService.getJwtBlackList(accessToken);
                if (blackListedToken != null) {
                    logger.error("JwtInterceptor: Token is blacklisted");
                    filterChain.doFilter(request, response);
                    return;
                }
                Jws<Claims> claimsJws = jwtUtils.getJwsClaims(accessToken);

                Claims body = claimsJws.getBody();
                String username = body.getSubject();
                List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
                Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                        .map(m -> new SimpleGrantedAuthority(m.get(AppStrings.AUTHORITY)))
                        .collect(Collectors.toSet());
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        grantedAuthorities
                );
                authentication.setDetails(accessToken);
                /**
                 * Spring (SecurityContextImpl) will use this authentication to check user authority against antMatchers() or APIs
                 * SecurityContextHolder is used to access SecurityContext from our project
                 */
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            // Write the error message to the response body
            response.getWriter().write(e.getMessage());
        } catch (JwtException e) {
            throw new IllegalStateException(
                    String.format("Token %s cannot be trusted!!", accessToken));
        }
    }

    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        Optional<String> stringRawCookie = Optional.ofNullable(request.getHeader(AppStrings.COOKIE));
        String[] stringCookie = new String[3];
        if (stringRawCookie.isPresent()) {
            stringCookie = request.getHeader(AppStrings.COOKIE).split(";");
        }
        String accessToken = stringCookie[0].replace("ACCESS_TOKEN=", "");
        String refreshToken = stringCookie[1].replace("REFRESH_TOKEN=", "");

        if (Strings.isNullOrEmpty(refreshToken) || Strings.isNullOrEmpty(accessToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Access or Refresh Token is Assigned");
        }
        blackListingService.blackListJwt(accessToken);

        if (!jwtUtils.validateJwtToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh Token is Invalid!");
        }
        String blackListedToken = blackListingService.getJwtBlackList(refreshToken);
        if (blackListedToken != null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Refresh Token is Expired!");
        }
        Jws<Claims> claimsJws = jwtUtils.getJwsClaims(refreshToken);
        Claims body = claimsJws.getBody();
        String username = body.getSubject();
        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
        Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get(AppStrings.AUTHORITY)))
                .collect(Collectors.toSet());
        String role = "";
        for (GrantedAuthority s : grantedAuthorities) {
            if (s.toString().contains("ROLE")) {
                if (s.toString().matches("ROLE_ADMIN"))
                    role = "admin";
                else
                    role = "user";
                break;
            }
        }
        int lifeTimeOfToken = Integer.parseInt(jwtExpireTime);
        Date dateExpAccessToken = jwtUtils.getTokenExpireDate(lifeTimeOfToken);
        Date dateExpRefreshToken = jwtUtils.getTokenExpireDate(lifeTimeOfToken * 84);

        Date dateIssuedAt = new Date();
        String newAccessToken = jwtUtils.generateTokenFromUsername(body.getSubject(), grantedAuthorities,
                dateIssuedAt, dateExpAccessToken);
        String newRefreshToken = jwtUtils.generateTokenFromUsername(body.getSubject(), grantedAuthorities,
                dateIssuedAt, dateExpRefreshToken);


        Cookie accessCookie = writeJwtResponse(response, username, role, dateExpAccessToken, dateIssuedAt, newAccessToken);

        response.addCookie(accessCookie);

        Cookie RefreshCookie = new Cookie(AppStrings.REFRESH_TOKEN, newRefreshToken);
        RefreshCookie.setHttpOnly(true);
        RefreshCookie.setSecure(true);

        response.addCookie(RefreshCookie);

        blackListingService.blackListJwt(refreshToken);

    }

    public static Cookie writeJwtResponse(HttpServletResponse response, String username, String role, Date dateExpAccessToken, Date dateIssuedAt, String newAccessToken) throws IOException {
        JSONObject jsonObjectOfResponseBody = new JSONObject();
        try {
            jsonObjectOfResponseBody.put(AppStrings.ISSUED_AT, dateIssuedAt.toString());
            jsonObjectOfResponseBody.put(AppStrings.EXPIRE_AT_ACCESS_TOKEN, dateExpAccessToken.toString());
            jsonObjectOfResponseBody.put(AppStrings.USERNAME, username);
            jsonObjectOfResponseBody.put(AppStrings.ROLE, role);
//            jsonObjectOfResponseBody.put(AppStrings.TOKEN, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        response.getWriter().write(jsonObjectOfResponseBody.toString());

        Cookie accessCookie = new Cookie(AppStrings.ACCESS_TOKEN, newAccessToken);
        accessCookie.setHttpOnly(true);
        return accessCookie;
    }
}


package io.welldev.initializer.configuration.authorization;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.welldev.initializer.configuration.authentication.JwtUtils;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.service.BlackListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
        if (stringRawCookie.isPresent()) {
            stringCookie = request.getHeader(AppStrings.COOKIE).split(";");
        } else {
            filterChain.doFilter(request, response);
            return;
        }
        String accessToken = stringCookie[0].replace("ACCESS_TOKEN=", "");
        String refreshToken = stringCookie[1].replace("REFRESH_TOKEN=", "");

        try {

            if (!jwtUtils.validateJwtToken(accessToken)) {
                throw new Exception();
            } else {
                String blackListedToken = blackListingService.getJwtBlackList(accessToken);
                if (blackListedToken != null) {
                    logger.error("JwtInterceptor: Token is blacklisted");
                    filterChain.doFilter(request, response);
                    return;
                }
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(System.getenv(AppStrings.TOKEN_SECRET_KEY).getBytes()))
                        .build()
                        /**
                         * parseClaimsJws(token) method verifies the token
                         */
                        .parseClaimsJws(accessToken);

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

        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("Token %s cannot be trusted!!", accessToken));
        }
        filterChain.doFilter(request, response);
    }

    public void refreshToken(HttpServletRequest request,
                             HttpServletResponse response) {
        String accessToken = request.getHeader(AppStrings.AUTHORIZATION);
        String refreshToken = request.getHeader(AppStrings.RENEW_AUTH);

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
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(System.getenv(AppStrings.TOKEN_SECRET_KEY).getBytes()))
                .build()
                /**
                 * parseClaimsJws(token) method verifies the token
                 */
                .parseClaimsJws(refreshToken);
        Claims body = claimsJws.getBody();
        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
        Set<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                .map(m -> new SimpleGrantedAuthority(m.get(AppStrings.AUTHORITY)))
                .collect(Collectors.toSet());
        int lifeTimeOfToken = Integer.parseInt(jwtExpireTime);
        Date dateExpAccessToken = jwtUtils.getTokenExpireDate(lifeTimeOfToken);
        Date dateExpRefreshToken = jwtUtils.getTokenExpireDate(lifeTimeOfToken * 84);

        Date dateIat = new Date();
        String newAccessToken = jwtUtils.generateTokenFromUsername(body.getSubject(), grantedAuthorities,
                dateIat, dateExpAccessToken);
        String newRefreshToken = jwtUtils.generateTokenFromUsername(body.getSubject(), grantedAuthorities,
                dateIat, dateExpRefreshToken);

        Cookie accessCookie = new Cookie(AppStrings.ACCESS_TOKEN, newAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);

        response.addCookie(accessCookie);

        Cookie RefreshCookie = new Cookie(AppStrings.REFRESH_TOKEN, newRefreshToken);
        RefreshCookie.setHttpOnly(true);
        RefreshCookie.setSecure(true);

        response.addCookie(RefreshCookie);

        blackListingService.blackListJwt(refreshToken);

    }
}


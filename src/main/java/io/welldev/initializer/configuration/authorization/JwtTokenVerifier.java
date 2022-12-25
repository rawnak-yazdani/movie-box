package io.welldev.initializer.configuration.authorization;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.welldev.model.constants.Constants.*;
import io.welldev.model.service.BlackListingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenVerifier extends OncePerRequestFilter {
    @Autowired
    private BlackListingService blackListingService;

    /**
     * This method will be called when user hits an API which requires authorization
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader(AppStrings.AUTHORIZATION);

        if (Strings.isNullOrEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(System.getenv(AppStrings.TOKEN_SECRET_KEY).getBytes()))
                    .build()
                    /**
                     * parseClaimsJws(token) method verifies the token
                     */
                    .parseClaimsJws(token);
            String blackListedToken = blackListingService.getJwtBlackList(token);
            if (blackListedToken != null) {
                logger.error("JwtInterceptor: Token is blacklisted");
                filterChain.doFilter(request, response);
                return;
            }
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
            authentication.setDetails(token);
            /**
             * Spring (SecurityContextImpl) will use this authentication to check user authority against antMatchers() or APIs
             * SecurityContextHolder is used to access SecurityContext from our project
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            throw new IllegalStateException(
                    String.format("Token %s cannot be trusted!!",token));
        }
        filterChain.doFilter(request, response);
    }
}


package io.welldev.initializer.configuration;

import io.welldev.initializer.configuration.authentication.AppUsernameAndPasswordAuthenticationFilter;
import io.welldev.initializer.configuration.authentication.JwtUtils;
import io.welldev.initializer.configuration.authorization.JwtTokenVerifier;
import io.welldev.initializer.configuration.authentication.AppUserDetailsService;
import io.welldev.model.exception.AppAuthenticationFailureHandler;
import io.welldev.model.role.Permissions;
import io.welldev.model.role.Roles;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AllArgsConstructor
@EnableWebSecurity
@ComponentScan({"io.welldev.initializer.configuration.authentication", "io.welldev.initializer.configuration.authorization"})
public class SecurityConfig {

    private PasswordEncoder passwordEncoder;
    
    private AppUserDetailsService userDetailsService;
    
    @Autowired
    private JwtTokenVerifier jwtTokenVerifier;
    private JwtUtils jwtUtils;

//    @Bean
//    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
//    public UserRequestScopeBean requestScopeBean() {
//        return new UserRequestScopeBean();
//    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        AppUsernameAndPasswordAuthenticationFilter appUsernameAndPasswordAuthenticationFilter =
                new AppUsernameAndPasswordAuthenticationFilter(authenticationManager, jwtUtils, authenticationFailureHandler());
        appUsernameAndPasswordAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return appUsernameAndPasswordAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        httpSecurity.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(usernamePasswordAuthenticationFilter(authenticationManager))
                .addFilterAfter(jwtTokenVerifier, AppUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Permissions.ADMIN_READ.getPermission())

                .antMatchers(HttpMethod.PUT, "/users/**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/users/**").hasAnyRole(Roles.ADMIN.name(), Roles.USER.name())

                .antMatchers(HttpMethod.POST, "/users").permitAll()
                .antMatchers(HttpMethod.GET,"/movies/**").permitAll()
                .antMatchers(HttpMethod.GET,"/token").permitAll()
                .anyRequest()
                .authenticated();
//                .antMatchers("/anonymous*")
//                .formLogin();
//                .loginPage("/login.html")
//                .loginProcessingUrl("/perform_login")
//                .defaultSuccessUrl("/index.jsp", true)
//                .failureUrl("/login.html?error=true")
//                .failureHandler(authenticationFailureHandler())
//                .and()
//                .logout()
//                .logoutUrl("/perform_logout")
//                .deleteCookies("ROBBIEID")
//                .logoutSuccessHandler(logoutSuccessHandler());

        return  httpSecurity.build();
    }



    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }
}

package io.welldev.initializer.configuration;

import io.welldev.initializer.configuration.userauth.AppUsernameAndPasswordAuthenticationFilter;
import io.welldev.initializer.configuration.userauth.JwtTokenVerifier;
import io.welldev.model.role.Permissions;
import io.welldev.model.role.Roles;
import io.welldev.model.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@EnableWebSecurity
@ComponentScan({"io.welldev.initializer.configuration.userauth"})
public class SecurityConfig {

    private PasswordEncoder passwordEncoder;
    private AppUserService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {
        httpSecurity.csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new AppUsernameAndPasswordAuthenticationFilter(authenticationManager))
                .addFilterAfter(new JwtTokenVerifier(), AppUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Permissions.ADMIN_READ.getPermission())

                .antMatchers(HttpMethod.POST, "/users**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/users**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.DELETE, "/users**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/users**")

                .hasAnyRole(Roles.ADMIN.name(), Roles.USER.name())
//                .antMatchers("/anonymous*")
                .antMatchers("/movies", "/signup/user", "/signup/main-admin")
                .permitAll()
                .anyRequest()
                .authenticated();
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
}

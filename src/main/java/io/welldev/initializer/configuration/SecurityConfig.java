package io.welldev.initializer.configuration;

import io.welldev.model.role.Permissions;
import io.welldev.model.role.Roles;
import io.welldev.model.service.AppUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private PasswordEncoder passwordEncoder;
    private AppUserDetailsService userDetailsService;
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails user1 = User.withUsername("Robbi")
//                .password(passwordEncoder().encode("user1Pass"))
////                .roles("USER")
//                .authorities(Roles.USER.grantedAuthorities())
//                .build();
//        UserDetails user2 = User.withUsername("Anik")
//                .password(passwordEncoder().encode("user2Pass"))
////                .roles("USER")
//                .authorities(Roles.USER.grantedAuthorities())
//                .build();
//        UserDetails admin = User.withUsername("Rawnak Yazdani")
//                .password(passwordEncoder().encode("adminPass"))
////                .roles("ADMIN")
//                .authorities(Roles.ADMIN.grantedAuthorities())
//                .build();
//        UserDetails adminTrainee = User.withUsername("Arnob")
//                .password(passwordEncoder().encode("traineePass"))
//                .authorities(Roles.ADMINTRAINEE.grantedAuthorities())
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2, admin, adminTrainee);
//    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/admin/**").hasAuthority(Permissions.ADMIN_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/admin/**").hasAuthority(Permissions.ADMIN_READ.getPermission())
                .antMatchers(HttpMethod.DELETE, "/users**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.POST, "/users**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.PUT, "/users**").hasAuthority(Permissions.USER_WRITE.getPermission())
                .antMatchers(HttpMethod.GET, "/users**")
                .hasAnyRole(Roles.ADMIN.name(), Roles.ADMINTRAINEE.name(), Roles.USER.name())
//                .antMatchers("/anonymous*")
                .antMatchers("/", "/signup")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
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

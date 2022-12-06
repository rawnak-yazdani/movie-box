package io.welldev.model.service;

import io.welldev.model.entity.AppUserDetails;
import io.welldev.model.repository.AppUserDetailsRepo;
import io.welldev.model.role.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserDetailsServiceImpl implements AppUserDetailsService {

    private final AppUserDetailsRepo appUserDetailsRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUserDetails user = (AppUserDetails) appUserDetailsRepo.findByUsername(username);
        if (user != null) {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Roles.USER.grantedAuthorities())
                    .build();
        } else throw new UsernameNotFoundException(String.format("Username %s not found", username));

    }

    public void save(AppUserDetails user) {
        appUserDetailsRepo.save(
                new AppUserDetails(
                        user.getId(),
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getCinephile()
                        )
        );
    }

    public AppUserDetails getUserByName(String username) {
        return (AppUserDetails) appUserDetailsRepo.findByUsername(username);
    }
}

package io.welldev.model.service;

import io.welldev.model.entity.AppUser;
import io.welldev.model.repository.AppUserRepo;
import io.welldev.model.role.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepo appUserRepo;
    private final CinephileService cinephileService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepo.findByUsername(username);
        if (user != null) {
            if (user.getRole().equals("user")) {
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(Roles.USER.grantedAuthorities())
                        .build();
            } else if (user.getRole().equals("admin")) {
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(Roles.ADMIN.grantedAuthorities())
                        .build();
            } else {
                return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(Roles.ADMINTRAINEE.grantedAuthorities())
                        .build();
            }

        } else throw new UsernameNotFoundException(String.format("Username %s not found", username));

    }

    public void save(AppUser user, String role) {
        if (appUserRepo.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException();
        } else {
            appUserRepo.save(
                    new AppUser(
                            user.getId(),
                            user.getUsername(),
                            user.getName(),
                            passwordEncoder.encode(user.getPassword()),
                            role,
                            user.getUserCreationDate(),
                            user.getWatchList()
                    )
            );
        }

    }

    public AppUser findCredentialsByUsername(String username) {
        return appUserRepo.findByUsername(username);
    }
}

package io.welldev.model.service;

import io.welldev.model.entity.Credentials;
import io.welldev.model.repository.AdminRepo;
import io.welldev.model.repository.CredentialsRepo;
import io.welldev.model.repository.CinephileRepo;
import io.welldev.model.role.Roles;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CredentialsServiceImpl implements CredentialsService {

    private final CredentialsRepo credentialsRepo;
    private final CinephileService cinephileService;
    private final AdminService adminService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials user = credentialsRepo.findByUsername(username);
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

    public void save(Credentials user, String role) {
        if (role.equals("user"))
            cinephileService.save(user.getCinephile());
        else
            adminService.save(user.getAdmin());

        credentialsRepo.save(
                new Credentials(
                        user.getId(),
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        role,
                        user.getCinephile(),
                        user.getAdmin()
                )
        );
    }

    public Credentials getUserByName(String username) {
        return credentialsRepo.findByUsername(username);
    }
}

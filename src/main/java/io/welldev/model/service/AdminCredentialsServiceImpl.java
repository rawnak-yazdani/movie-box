package io.welldev.model.service;

import io.welldev.model.entity.AdminCredentials;
import io.welldev.model.repository.AdminCredentialsRepo;
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
public class AdminCredentialsServiceImpl implements AdminCredentialsService {

    private final AdminCredentialsRepo adminCredentialsRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(AdminCredentials user) {
        adminCredentialsRepo.save(
                new AdminCredentials(
                        user.getId(),
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getAdmin()
                )
        );

    }

    @Override
    public AdminCredentials getUserByName(String username) {
        return (AdminCredentials) adminCredentialsRepo.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminCredentials user = (AdminCredentials) adminCredentialsRepo.findByUsername(username);
        if (user != null) {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Roles.USER.grantedAuthorities())
                    .build();
        } else throw new UsernameNotFoundException(String.format("Username %s not found", username));
    }
}

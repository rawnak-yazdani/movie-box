package io.welldev.model.service;

import io.welldev.model.entity.CinephileCredentials;
import io.welldev.model.repository.CinephileCredentialsRepo;
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
public class CinephileCredentialsServiceImpl implements CinephileCredentialsService {

    private final CinephileCredentialsRepo cinephileCredentialsRepo;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CinephileCredentials user = (CinephileCredentials) cinephileCredentialsRepo.findByUsername(username);
        if (user != null) {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Roles.USER.grantedAuthorities())
                    .build();
        } else throw new UsernameNotFoundException(String.format("Username %s not found", username));

    }

    public void save(CinephileCredentials user) {
        cinephileCredentialsRepo.save(
                new CinephileCredentials(
                        user.getId(),
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        user.getCinephile()
                        )
        );
    }

    public CinephileCredentials getUserByName(String username) {
        return (CinephileCredentials) cinephileCredentialsRepo.findByUsername(username);
    }
}

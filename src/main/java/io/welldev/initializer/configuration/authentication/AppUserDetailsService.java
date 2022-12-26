package io.welldev.initializer.configuration.authentication;

import io.welldev.model.constants.Constants.AppStrings;
import io.welldev.model.entity.AppUser;
import io.welldev.model.role.Roles;
import io.welldev.model.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserService appUserService;

    /**
     * This method will be called second during login [from authenticationManager.authenticate(authentication)]
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, ResponseStatusException {
        AppUser user = appUserService.findAppUserByUsername(username);

        if (user.getRole().equals(AppStrings.USER_ROLE)) {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Roles.USER.grantedAuthorities())
                    .build();
        } else {
            return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Roles.ADMIN.grantedAuthorities())
                    .build();
        }

    }
}

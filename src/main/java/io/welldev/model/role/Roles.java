package io.welldev.model.role;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static io.welldev.model.role.Permissions.*;

public enum Roles {
    USER(Sets.newHashSet(USER_READ, USER_WRITE)),

    ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, ADMIN_READ, ADMIN_WRITE));

    private final Set<Permissions> permissions;

    Roles(Set<Permissions> permissions) {
        this.permissions = permissions;
    }

    public Set<Permissions> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> grantedAuthorities() {
        Set<SimpleGrantedAuthority> grantedAuthoritySet = getPermissions().stream()
                .map(permissions1 -> new SimpleGrantedAuthority(permissions1.getPermission()))
                .collect(Collectors.toSet());
        grantedAuthoritySet.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthoritySet;
    }
}

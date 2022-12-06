package io.welldev.model.repository;

import io.welldev.model.entity.AppUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppUserDetailsRepo extends JpaRepository<AppUserDetails,Long> {
    AppUserDetails findByUsername(String username);
}

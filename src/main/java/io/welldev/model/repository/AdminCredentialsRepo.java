package io.welldev.model.repository;

import io.welldev.model.entity.AdminCredentials;
import io.welldev.model.entity.CinephileCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminCredentialsRepo extends JpaRepository<AdminCredentials,Long> {
    AdminCredentials findByUsername(String username);
}

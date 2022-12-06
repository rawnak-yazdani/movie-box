package io.welldev.model.repository;

import io.welldev.model.entity.CinephileCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinephileCredentialsRepo extends JpaRepository<CinephileCredentials,Long> {
    CinephileCredentials findByUsername(String username);
}

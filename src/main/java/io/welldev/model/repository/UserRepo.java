package io.welldev.model.repository;

import io.welldev.model.entity.Cinephile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Cinephile, Long> {
}

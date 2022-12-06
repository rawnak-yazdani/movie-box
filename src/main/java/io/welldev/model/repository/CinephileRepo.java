package io.welldev.model.repository;

import io.welldev.model.entity.Cinephile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinephileRepo extends JpaRepository<Cinephile, Long> {
}

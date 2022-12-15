package io.welldev.model.repository;

import io.welldev.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findByName(String name);
}

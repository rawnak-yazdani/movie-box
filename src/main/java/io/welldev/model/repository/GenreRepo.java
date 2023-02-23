package io.welldev.model.repository;

import io.welldev.model.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepo extends JpaRepository<Genre, Long> {
    Genre findByName(String name);

    @Modifying
    @Query(value = "insert into genre (id, name) values (nextval('genre_seq'), :name) ON CONFLICT (name) DO NOTHING",
            nativeQuery = true)
    void addGenresOnStartUp(@Param("name") String name);
}

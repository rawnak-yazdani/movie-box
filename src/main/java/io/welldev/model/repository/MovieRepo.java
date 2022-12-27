package io.welldev.model.repository;

import io.welldev.model.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepo extends JpaRepository<Movie, Long> {

    @Modifying
    @Query(value = "delete from movie_app_user where movie_id = :id",
    nativeQuery = true)
    void deleteUserAndMovieAssociation(@Param("id") Long id);
}

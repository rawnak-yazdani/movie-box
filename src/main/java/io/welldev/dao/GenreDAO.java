package io.welldev.dao;

import io.welldev.model.Genre;
import io.welldev.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class GenreDAO {
    @PersistenceContext
    private EntityManager entityManager;

    public void add(Genre genre) {
        entityManager.persist(genre);
    }

    public void addAll(List<Genre> genreList) {
        for (Genre g:
                genreList) {
            entityManager.persist(g);
        }
    }

    public Genre get(Long id) {
        return entityManager.find(Genre.class, id);
    }

    public void delete(Long id) {
        entityManager.remove(entityManager.find(Genre.class, id));
    }

    public List<Genre> getAll() {
        List<Genre> resultList = entityManager.createQuery("from Genre").getResultList();

        return resultList;
    }

    public void update(Genre genre) {
        entityManager.merge(genre);
    }
}

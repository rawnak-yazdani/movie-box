package io.welldev.service;

import io.welldev.model.Director;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class DirectorDAO {
    @PersistenceContext
    private EntityManager entityManager;



    public void add(Director director) {
        entityManager.persist(director);
    }

    public void addAll(List<Director> directorList) {
        for (Director d:
                directorList) {
            entityManager.persist(d);
        }
    }

    public void addWithoutTransaction(Director director) {
        entityManager.persist(director);
    }

    public Director get(Long id) {
        return entityManager.find(Director.class, id);
    }

    public void delete(Long id) {
        entityManager.remove(entityManager.find(Director.class, id));
    }

    public List<Director> getAll() {
        List<Director> resultList = entityManager.createQuery("from Director").getResultList();

        return resultList;
    }

    public void update(Director director) {
        entityManager.merge(director);
    }
}

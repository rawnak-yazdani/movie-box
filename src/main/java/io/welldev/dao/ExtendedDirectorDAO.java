package io.welldev.dao;

import io.welldev.model.Director;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ExtendedDirectorDAO {
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;



    @Transactional
    public void add(Director director) {
        entityManager.persist(director);
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

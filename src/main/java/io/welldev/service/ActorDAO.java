package io.welldev.service;

import io.welldev.model.Actor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class ActorDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void add(Actor actor) {
        entityManager.persist(actor);
    }

    public void addAll(List<Actor> actorList) {
        for (Actor a:
                actorList) {
            entityManager.persist(a);
        }
    }

    public Actor get(Long id) {

        return entityManager.find(Actor.class, id);
    }

    public void delete(Long id) {
        Actor actor = entityManager.find(Actor.class, id);
        entityManager.remove(entityManager.find(Actor.class, id));
    }

    public List<Actor> getAll() {
        List<Actor> resultList = entityManager.createQuery("from Actor").getResultList();

        return resultList;
    }

    public void update(Actor actor) {
        entityManager.merge(actor);
    }
}

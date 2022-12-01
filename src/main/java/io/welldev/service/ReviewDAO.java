package io.welldev.service;

import io.welldev.model.Review;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@Transactional
public class ReviewDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void add(Review review) {
        entityManager.persist(review);
    }

    public void addAll(List<Review> reviewList) {
        for (Review r:
                reviewList) {
            entityManager.persist(r);
        }
    }

    public Review get(Long id) {
        return entityManager.find(Review.class, id);
    }

    public void delete(Long id) {
        entityManager.remove(entityManager.find(Review.class, id));
    }

    public List<Review> getAll() {
        List<Review> resultList = entityManager.createQuery("from Review").getResultList();

        return resultList;
    }

    public void update(Review review) {
        entityManager.merge(review);
    }
}

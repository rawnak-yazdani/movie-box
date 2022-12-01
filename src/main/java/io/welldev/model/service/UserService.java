package io.welldev.model.service;

import io.welldev.model.entity.User;
import io.welldev.model.repository.UserRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class UserService {
    public final UserRepo userRepo;

    public void save(User user) {
        userRepo.save(user);
    }

    public void flush() {
        userRepo.flush();
    }

    public void saveAndFlush(User user) {
        userRepo.saveAndFlush(user);
    }

    //
    public void saveAll(List<User> usersList) {
        userRepo.saveAll(usersList);
    }

    public void saveAllAndFlush(List<User> usersList) {
        userRepo.saveAllAndFlush(usersList);
    }

    public User findById(Long id) {
        return userRepo.findById(id).get();
    }

    public List<User> findAllById() {
        return userRepo.findAll();
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public void delete(User user) {
        userRepo.delete(user);
    }

    public void deleteAllById(List<Long> ids) {
        userRepo.deleteAllById(ids);
    }

    public void deleteAll(List<User> users) {
        userRepo.deleteAll(users);
    }

    public void deleteAll() {
        userRepo.deleteAll();
    }

}

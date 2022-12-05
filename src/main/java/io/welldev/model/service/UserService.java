package io.welldev.model.service;

import io.welldev.model.entity.Cinephile;
import io.welldev.model.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class UserService {
    public final UserRepo userRepo;

    public void save(Cinephile cinephile) {
        userRepo.save(cinephile);
    }

    public void flush() {
        userRepo.flush();
    }

    public void saveAndFlush(Cinephile cinephile) {
        userRepo.saveAndFlush(cinephile);
    }

    //
    public void saveAll(List<Cinephile> usersList) {
        userRepo.saveAll(usersList);
    }

    public void saveAllAndFlush(List<Cinephile> usersList) {
        userRepo.saveAllAndFlush(usersList);
    }

    public Cinephile findById(Long id) {
        return userRepo.findById(id).get();
    }

    public List<Cinephile> findAll() {
        return userRepo.findAll();
    }

    public void deleteById(Long id) {
        userRepo.deleteById(id);
    }

    public void delete(Cinephile cinephile) {
        userRepo.delete(cinephile);
    }

    public void deleteAllById(List<Long> ids) {
        userRepo.deleteAllById(ids);
    }

    public void deleteAll(List<Cinephile> cinephiles) {
        userRepo.deleteAll(cinephiles);
    }

    public void deleteAll() {
        userRepo.deleteAll();
    }

}

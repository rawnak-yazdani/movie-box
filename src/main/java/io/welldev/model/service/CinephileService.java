package io.welldev.model.service;

import io.welldev.model.entity.Cinephile;
import io.welldev.model.repository.CinephileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CinephileService {
    public final CinephileRepo cinephileRepo;

    public void save(Cinephile cinephile) {
        cinephileRepo.save(cinephile);
    }

    public void flush() {
        cinephileRepo.flush();
    }

    public void saveAndFlush(Cinephile cinephile) {
        cinephileRepo.saveAndFlush(cinephile);
    }

    //
    public void saveAll(List<Cinephile> usersList) {
        cinephileRepo.saveAll(usersList);
    }

    public void saveAllAndFlush(List<Cinephile> usersList) {
        cinephileRepo.saveAllAndFlush(usersList);
    }

    public Cinephile findById(Long id) {
        return cinephileRepo.findById(id).get();
    }

    public List<Cinephile> findAll() {
        return cinephileRepo.findAll();
    }

    public void deleteById(Long id) {
        cinephileRepo.deleteById(id);
    }

    public void delete(Cinephile cinephile) {
        cinephileRepo.delete(cinephile);
    }

    public void deleteAllById(List<Long> ids) {
        cinephileRepo.deleteAllById(ids);
    }

    public void deleteAll(List<Cinephile> cinephiles) {
        cinephileRepo.deleteAll(cinephiles);
    }

    public void deleteAll() {
        cinephileRepo.deleteAll();
    }

}

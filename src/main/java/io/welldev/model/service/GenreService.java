package io.welldev.model.service;

import io.welldev.model.entity.Genre;
import io.welldev.model.repository.GenreRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Component
@Transactional
public class GenreService {
    public final GenreRepo genreRepo;

    public void save(Genre genre) {
        genreRepo.save(genre);
    }

    public void flush() {
        genreRepo.flush();
    }

    public void saveAndFlush(Genre genre) {
        genreRepo.saveAndFlush(genre);
    }

    //
    public void saveAll(List<Genre> genresList) {
        genreRepo.saveAll(genresList);
    }

    public void saveAllAndFlush(List<Genre> genresList) {
        genreRepo.saveAllAndFlush(genresList);
    }

    public Genre findById(Long id) {
        return genreRepo.findById(id).get();
    }

    public List<Genre> findAll() {
        return genreRepo.findAll();
    }

    public void deleteById(Long id) {
        genreRepo.deleteById(id);
    }

    public void delete(Genre genre) {
        genreRepo.delete(genre);
    }

    public void deleteAllById(List<Long> ids) {
        genreRepo.deleteAllById(ids);
    }

    public void deleteAll(List<Genre> genres) {
        genreRepo.deleteAll(genres);
    }

    public void deleteAll() {
        genreRepo.deleteAll();
    }
}

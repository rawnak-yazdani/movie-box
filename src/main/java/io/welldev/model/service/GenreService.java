package io.welldev.model.service;

import io.welldev.model.entity.Genre;
import io.welldev.model.exception.ItemNotFoundException;
import io.welldev.model.repository.GenreRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Service
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

    public void saveAll(Set<Genre> genresList) {
        genreRepo.saveAll(genresList);
    }

    public void saveAllAndFlush(List<Genre> genresList) {
        genreRepo.saveAllAndFlush(genresList);
    }

    public Genre findById(Long id) {
        return genreRepo.findById(id).get();
    }

    public List<Genre> findAll() {
        Optional<List<Genre>> optionalGenres = Optional.ofNullable(genreRepo.findAll());
        if (optionalGenres.isPresent()) {
            return optionalGenres.get();
        } else {
            throw new ItemNotFoundException("No Genre has been added");
        }
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

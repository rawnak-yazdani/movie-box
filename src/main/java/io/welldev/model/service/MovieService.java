package io.welldev.model.service;

import io.welldev.model.entity.Genre;
import io.welldev.model.entity.Movie;
import io.welldev.model.repository.GenreRepo;
import io.welldev.model.repository.MovieRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
@Service
@Transactional
public class MovieService {
    public final MovieRepo movieRepo;

    public final GenreRepo genreRepo;

    public Movie save(Movie movie) {
        Set<Genre> genres = movie.getGenres();
//        Iterator<Genre> genreIterator = genres.iterator();
        for (Genre genre:
             genres) {
            Genre existingGenre = genreRepo.findByName(genre.getName());
            if (existingGenre != null) {
                genre.setId(existingGenre.getId());
            } else genreRepo.save(genre);
        }
        return movieRepo.save(movie);
    }

    public void flush() {
        movieRepo.flush();
    }

    public void saveAndFlush(Movie movie) {
        movieRepo.saveAndFlush(movie);
    }

    public void saveAll(List<Movie> moviesList) {
        movieRepo.saveAll(moviesList);
    }

    public void saveAllAndFlush(List<Movie> moviesList) {
        movieRepo.saveAllAndFlush(moviesList);
    }

    public Movie findById(Long id) {
        return movieRepo.findById(id).get();
    }

    public List<Movie> findAll() {
        return movieRepo.findAll();
    }

    public void deleteById(Long id) {
        movieRepo.deleteById(id);
    }

    public void delete(Movie movie) {
        movieRepo.delete(movie);
    }

    public void deleteAllById(List<Long> ids) {
        movieRepo.deleteAllById(ids);
    }

    public void deleteAll(List<Movie> movies) {
        movieRepo.deleteAll(movies);
    }

    public void deleteAll() {
        movieRepo.deleteAll();
    }
}

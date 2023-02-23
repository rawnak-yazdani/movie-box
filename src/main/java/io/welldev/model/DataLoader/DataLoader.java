package io.welldev.model.DataLoader;

import io.welldev.model.repository.GenreRepo;
import io.welldev.model.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Transactional
public class DataLoader implements CommandLineRunner {

    private final GenreService genreService;
    private final GenreRepo genreRepo;
    @Override
    public void run(String... args) throws Exception {
        Set<String> movieGenres = new HashSet<>(Arrays.asList(
                "Action",
                "Adventure",
                "Animation",
                "Biography",
                "Comedy",
                "Crime",
                "Documentary",
                "Drama",
                "Family",
                "Fantasy",
                "Film-Noir",
                "History",
                "Horror",
                "Music",
                "Musical",
                "Mystery",
                "Romance",
                "Sci-Fi",
                "Short",
                "Sport",
                "Superhero",
                "Thriller",
                "War",
                "Western"
        ));

        for (String genreName : movieGenres) {
            genreRepo.addGenresOnStartUp(genreName);
        }

    }
}

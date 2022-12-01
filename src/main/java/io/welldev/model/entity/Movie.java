package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = {@JoinColumn(name = "fk_movie")},
            inverseJoinColumns = {@JoinColumn(name = "fk_genre")})
    @Fetch(value = FetchMode.SELECT)
    private Set<Genre> genres = new HashSet<Genre>();

    private String rating;

    private int year;
}

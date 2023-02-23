package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Movie {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue=1, name = "movie_seq", sequenceName = "movie_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "movie_seq")
    private Long id;

    private String title;

    private String imgSrc;

    private String description;

    @ManyToMany/*(fetch = FetchType.EAGER)*/
    @JoinTable(name = "movie_genre",
            joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Genre> genres = new HashSet<Genre>();

    private String rating;

    private Integer year;
}

//drop table movie, genre, app_user, movie_app_user, movie_genre;
//drop sequence movie_seq, user_seq, genre_seq;

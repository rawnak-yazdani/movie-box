package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "cinephile")
public class Cinephile {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue=1, name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @NotNull(message = "Name Required")
    @Pattern(regexp = "^[a-zA-Z0-9]", message = "Only alphabetical characters are allowed")
    private String name;

    @ManyToMany
    @JoinTable(name = "movie_cinephile",
            joinColumns = {@JoinColumn(name = "fk_cinephile")},
            inverseJoinColumns = {@JoinColumn(name = "fk_movie")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Movie> watchList = new HashSet<>();
}

package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class AppUser {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue = 1, name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    private String name;

    private String username;

    private String password;

    private String role;

    private String imgSrc;

    private java.sql.Timestamp userCreationDate = java.sql.Timestamp.valueOf(LocalDateTime.now());

    @ManyToMany
    @JoinTable(name = "movie_app_user",
            joinColumns = {@JoinColumn(name = "app_user_id")},
            inverseJoinColumns = {@JoinColumn(name = "movie_id")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Movie> watchlist = new HashSet<>();

}

package io.welldev.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue = 1, name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z-\\s]+$", message = "Only alphabetical characters are allowed")
    private String name;

    @NotNull(message = "Username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,8}", message = "Username length should be minimum 3 characters and maximum 8 characters")
    private String username;

    @NotNull(message = "Password is required")
    private String password;

    private String role;

    private String userCreationDate;

    @ManyToMany
    @JoinTable(name = "movie_app_user",
            joinColumns = {@JoinColumn(name = "fk_app_user")},
            inverseJoinColumns = {@JoinColumn(name = "fk_movie")})
    @Fetch(value = FetchMode.JOIN)
    private Set<Movie> watchList = new HashSet<>();
}
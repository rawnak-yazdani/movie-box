package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue = 1, name = "admin_seq", sequenceName = "admin_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq")
    private Long id;

    private String name;

    public Admin(String name) {
        this.name = name;
    }

}

/*
 * drop table admin, admin_credentials, cinephile, cinephile_credentials, genre, movie, movie_cinephile, movie_genre, movie_user;
 * */

package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Genre {
    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue=1, name = "genre_seq", sequenceName = "genre_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_seq")
    private Long id;

    private String name;

}

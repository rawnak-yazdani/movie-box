package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class AdminCredentials {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue = 1, name = "admin_seq", sequenceName = "admin_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq")
    private Long id;
    private String username;
    private String password;

    @OneToOne
    private Admin admin;

}

package io.welldev.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "credentials")
public class Credentials {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue = 1, name = "cred_seq", sequenceName = "cred_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cred_seq")
    private Long id;

    @NotNull(message = "username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,8}", message = "Minimum 3 characters long and maximum 8 characters")
    private String username;
    @NotNull(message = "password is required")
    private String password;
    private String role;

    @OneToOne
    private Cinephile cinephile;
    @OneToOne
    private Admin admin;

}

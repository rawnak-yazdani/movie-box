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
@Table(name = "admin_credentials")
public class AdminCredentials {

    @Id
    @Column(name = "id", nullable = false)
    @SequenceGenerator(initialValue = 1, name = "admin_seq", sequenceName = "admin_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "admin_seq")
    private Long id;
    @NotNull(message = "username is required")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,8}", message = "Minimum 3 characters long and maximum 8 characters")
    private String username;
    @NotNull(message = "password is required")
    private String password;

    @OneToOne
    private Admin admin;

}

package org.tolking.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false, updatable = false)
    private String username;

    @Column(nullable = false)
    @Size(min = 3, max = 100)
    private String password;

    @Column(name = "is_active", nullable = false)
    Boolean isActive;
}

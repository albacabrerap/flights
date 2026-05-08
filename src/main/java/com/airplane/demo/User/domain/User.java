package com.airplane.demo.User.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank
    private String firstName;
    @NotBlank private String lastName;
    @Email @NotBlank private String email;
    @NotBlank private String password; 
}

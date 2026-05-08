package com.airplane.demo.User.DTOs;
import lombok.Data;

@Data
public class RegisterUserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

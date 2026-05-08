package com.airplane.demo.User.domain;

import com.airplane.demo.User.DTOs.NewIdDTO;
import com.airplane.demo.User.DTOs.RegisterUserDTO;
import com.airplane.demo.User.DTOs.UserDTO;
import com.airplane.demo.User.infraestructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public NewIdDTO register(RegisterUserDTO dto) {
        if (dto.getFirstName() == null || dto.getFirstName().isBlank() || dto.getLastName() == null || dto.getLastName().isBlank() || dto.getEmail() == null || dto.getEmail().isBlank() || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All fields are required");
        }
        if (!dto.getEmail().matches("^[a-zA-Z0-9._]+@[a-zA-Z0-9._]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2})?$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }
        if (!dto.getFirstName().matches(".*[A-Z].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name must contain at least one uppercase letter");
        }
        if (!dto.getLastName().matches(".*[A-Z].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name must contain at least one uppercase letter");
        }
        if (!dto.getPassword().matches("^(?=.*[A-Za-z])(?=.*\\d).{8,}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters with at least one letter and one number");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        User saved = userRepository.save(user);
        return new NewIdDTO(saved.getId());
    }

    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return toDTO(user);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
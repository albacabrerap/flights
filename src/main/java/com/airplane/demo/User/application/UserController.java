package com.airplane.demo.User.application;

import com.airplane.demo.User.domain.UserService;
import com.airplane.demo.User.DTOs.NewIdDTO;
import com.airplane.demo.User.DTOs.RegisterUserDTO;
import com.airplane.demo.User.DTOs.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<NewIdDTO> register(@RequestBody RegisterUserDTO dto) {
        NewIdDTO result = userService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        UserDTO result = userService.getUserById(id);
        return ResponseEntity.ok(result);
    }
}



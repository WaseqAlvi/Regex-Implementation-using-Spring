package com.example.checkXampp.Controller;

import com.example.checkXampp.Model.User;
import com.example.checkXampp.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {


    private final UserRepo userRepository;

    @Autowired
    public UserController(UserRepo userRepository) {
        this.userRepository = userRepository;
    }

    // GET mapping to retrieve all users
    @GetMapping("/")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMap);
        }



        // Custom validation for password regex
        if (!user.getPassword().
                matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("password", "Password must be at least 8 characters long, contain at least one digit, " +
                    "one uppercase and one lowercase letter, and one special character.");
            return ResponseEntity.badRequest().body(errorMap);
        }
        if (!user.getEmail().matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")) {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("email", "Invalid email format");
            return ResponseEntity.badRequest().body(errorMap);
        }

        // If validation passes, save the user
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }





    // GET mapping to retrieve a specific user by ID
    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") long id) {
        return userRepository.findById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") long id,@Valid @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(updatedUser.getName());
                    user.setEmail(updatedUser.getEmail());
                    user.setPassword(updatedUser.getPassword());
                    user.setPhone(updatedUser.getPhone());
                    return userRepository.save(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") long id) {
        userRepository.deleteById(id);
    }
}

















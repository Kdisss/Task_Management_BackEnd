package com.kkd.task.user.service.controller;
import com.kkd.task.user.service.model.User;
import com.kkd.task.user.service.repository.UserRepository;
import com.kkd.task.user.service.response.AuthResponse;
import com.kkd.task.user.service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
   @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

   @GetMapping("/profile")
    public ResponseEntity<User>getUserProfile(@RequestHeader("Authorization")String jwt){

        User user = userService.getUserProfile(jwt);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping()
    public ResponseEntity <List<User>> getUsers(@RequestHeader("Authorization")String jwt){

        List<User> users= userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<AuthResponse> updateUserHandler(@RequestBody User user) throws Exception {
        // Extract email from the request
        String email = user.getEmail();

        // Check if the user exists in the database
        User existingUser = userRepository.findByEmail(email);

        if (existingUser == null) {
            throw new Exception("User not found with email: " + email);
        }

        // Update the user details
        if (user.getFullName() != null && !user.getFullName().isEmpty()) {
            existingUser.setFullName(user.getFullName());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getRole() != null && !user.getRole().isEmpty()) {
            existingUser.setRole(user.getRole());
        }

        // Save the updated user to the database
        userRepository.save(existingUser);

        // Prepare and return the response
        AuthResponse response = new AuthResponse();
        response.setMessage("User details updated successfully");
        response.setStatus(true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

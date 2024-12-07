package com.kkd.task.user.service.controller;

import com.kkd.task.user.service.config.JwtProvider;
import com.kkd.task.user.service.model.User;
import com.kkd.task.user.service.repository.UserRepository;
import com.kkd.task.user.service.request.LoginRequest;
import com.kkd.task.user.service.response.AuthResponse;
import com.kkd.task.user.service.service.CustomerUserServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLOutput;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerUserServiceImplementation customUserDetails;

    //signup method
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(
            @RequestBody User user) throws Exception{
        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();
        String role = user.getRole();

        User isEmailExist = userRepository.findByEmail(email);

        if(isEmailExist != null){
            throw new Exception("Email Already Exists");
        }

        //Create new user
        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setFullName(fullName);
        createdUser.setRole(role);
        createdUser.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(createdUser);


        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Registered Successfully");
        authResponse.setStatus(true);

        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);
    }


    //signin method
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest loginRequest){

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        System.out.println(username + "-----" + password);

        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = JwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();

        authResponse.setMessage("Login Successfully");
        authResponse.setJwt(token);
        authResponse.setStatus(true);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
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


    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetails.loadUserByUsername(username);

        System.out.println("sign in userDetails - null" + userDetails);

        //if no password is entered
        if(userDetails == null){
            System.out.println("sign in userDetails - null" + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }

        //here we compare password that user gave with password in database
        if(!passwordEncoder.matches(password, userDetails.getPassword())){
            System.out.println("sign in userDetails - password does not match" + userDetails);
            throw new BadCredentialsException("Invalid username or password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}

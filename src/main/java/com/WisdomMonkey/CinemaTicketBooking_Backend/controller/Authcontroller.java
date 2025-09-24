package com.WisdomMonkey.CinemaTicketBooking_Backend.controller;

import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.AuthRequest;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.AuthResponse;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UserResponse;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.security.JwtService;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserService;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class Authcontroller {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request) {
        // Check if user already exists
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        // Create new user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstname(request.getName());
        user.setLastname("DEFAULT"); // Frontend only collects name
        user.setUsername(request.getEmail()); // Use email as username
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userService.save(user);

        // Generate JWT Token
        String token = jwtService.generateToken(savedUser.getEmail());

        // Create response
        UserResponse userResponse = convertToUserResponse(savedUser, request.getProvider());
        AuthResponse authResponse = new AuthResponse(userResponse, token);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        // Find user by email
        User user = userService.findByEmail(request.getEmail()).orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().build();
        }

        // Update last login
        user.setLastLogin(LocalDateTime.now());
        userService.save(user);

        // Generate JWT Token
        String token = jwtService.generateToken(user.getEmail());

        // Create response
        UserResponse userResponse = convertToUserResponse(user, "email");
        AuthResponse authResponse = new AuthResponse(userResponse, token);

        return ResponseEntity.ok(authResponse);
    }

    private UserResponse convertToUserResponse(User user, String provider) {
        UserResponse response = new UserResponse();
        response.setId(user.getId().toString());
        response.setEmail(user.getEmail());
        response.setName(user.getFirstname());
        response.setProvider(provider);
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }

}

package com.WisdomMonkey.CinemaTicketBooking_Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String password;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @NotBlank
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstname;

    @NotBlank
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastname;

    @Column(name = "avatar", length = 500)
    private String avatar; // Avatar URL

    @Column(nullable = false)
    private boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // Mobile app specific fields
    @Column(name = "push_token", length = 500)
    private String pushToken;

    @Column(name = "device_type", length = 20)
    private String deviceType;

    @Column(name = "provider", length = 20)
    private String provider = "email"; // By Default

    @Column(name = "provider_id", length = 100)
    private String providerId; // For OAuth account linking
}

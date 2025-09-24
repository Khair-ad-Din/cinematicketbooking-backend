package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import java.time.LocalDateTime;

public class UserResponse {
    private String id;
    private String email;
    private String name;
    private String avatar; // Optional
    private String provider;
    private String providerId; // Optional, for OAuth accounts
    private LocalDateTime createdAt;

    public UserResponse() {
    }

    public UserResponse(String id, String email, String name, String provider, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

public class AuthResponse {
    private UserResponse user;
    private String token;
    private String refreshToken; // TODO: For future.

    public AuthResponse() {
    }

    public AuthResponse(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public AuthResponse(UserResponse user, String token, String refreshToken) {
        this.user = user;
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}

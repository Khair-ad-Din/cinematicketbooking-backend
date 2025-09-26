package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserProfileDto {

    // Basic Identity (required)
    private String id;
    private String username;
    private String email;
    private String displayName;

    // Profile Details (optional)
    private String avatarUrl;
    private String bio;
    private Boolean isPublic;

    // Statistics (computed)
    private Integer movieCount;
    private List<String> favoriteGenres;

    // Timestamps (formatted as string for frontend)
    private String joinedDate;
    private String lastActive;

}

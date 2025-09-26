package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import java.util.List;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileDto {

    @Size(min = 2, max = 50, message = "Display name must be between 2 and 50 characters")
    private String displayName;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    private Boolean isPublic;

    private List<String> favoriteGenres;
}

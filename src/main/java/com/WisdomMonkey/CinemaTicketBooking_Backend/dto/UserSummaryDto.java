package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import lombok.Data;

@Data
public class UserSummaryDto {
    private String id;
    private String username;
    private String displayName;
    private String avatarUrl;
    private Boolean isOnline;
}
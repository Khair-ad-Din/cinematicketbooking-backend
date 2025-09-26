package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import lombok.Data;

@Data
public class UserConnectionDto {
    private String id;
    private UserSummaryDto friend;
    private String status;
    private String createdAt;
}

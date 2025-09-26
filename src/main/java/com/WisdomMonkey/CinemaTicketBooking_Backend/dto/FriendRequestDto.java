package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import lombok.Data;

@Data
public class FriendRequestDto {
    private String id;
    private UserSummaryDto fromUser;
    private UserSummaryDto toUser;
    private String message;
    private String status;
    private String createdAt;
}

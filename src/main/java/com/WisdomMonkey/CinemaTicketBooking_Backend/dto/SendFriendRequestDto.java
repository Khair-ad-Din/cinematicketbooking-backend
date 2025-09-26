package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import lombok.Data;

@Data
public class SendFriendRequestDto {
    private String toUserId;
    private String message;
}

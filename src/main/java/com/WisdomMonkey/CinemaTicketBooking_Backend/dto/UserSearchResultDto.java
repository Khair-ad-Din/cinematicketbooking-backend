package com.WisdomMonkey.CinemaTicketBooking_Backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserSearchResultDto {
    private List<UserSummaryDto> users;
    private Integer totalCount;
    private Integer currentPage;
    private Integer totalPages;
}

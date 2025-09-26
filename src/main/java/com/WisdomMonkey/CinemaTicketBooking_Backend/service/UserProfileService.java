package com.WisdomMonkey.CinemaTicketBooking_Backend.service;

import java.util.Optional;

import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UpdateProfileDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserProfile;

public interface UserProfileService {

    Optional<UserProfile> findByUser(User user);

    Optional<UserProfile> findByUserId(Long userId);

    UserProfile createOrUpdateProfile(User user, UpdateProfileDto updateDto);

    void deleteByUserId(Long userId);
}

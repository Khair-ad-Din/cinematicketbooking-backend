package com.WisdomMonkey.CinemaTicketBooking_Backend.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UpdateProfileDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserProfile;
import com.WisdomMonkey.CinemaTicketBooking_Backend.repository.UserProfileRepository;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserProfileService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    public Optional<UserProfile> findByUser(User user) {
        return userProfileRepository.findByUser(user);
    }

    public Optional<UserProfile> findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId);
    }

    public UserProfile createOrUpdateProfile(User user, UpdateProfileDto updateDto) {
        UserProfile profile = userProfileRepository.findByUser(user)
                .orElse(new UserProfile());

        // Set user if this is a new profile
        if (profile.getUser() == null) {
            profile.setUser(user);
        }

        // Update profile fields
        if (updateDto.getBio() != null) {
            profile.setBio(updateDto.getBio());
        }

        if (updateDto.getIsPublic() != null) {
            UserProfile.ProfileVisibility visibility = updateDto.getIsPublic()
                    ? UserProfile.ProfileVisibility.PUBLIC
                    : UserProfile.ProfileVisibility.FRIENDS_ONLY;
            profile.setProfileVisibility(visibility);
        }

        if (updateDto.getFavoriteGenres() != null) {
            String genresString = String.join(",", updateDto.getFavoriteGenres());
            profile.setFavoriteGenres(genresString);
        }

        return userProfileRepository.save(profile);
    }

    public void deleteByUserId(Long userId) {
        userProfileRepository.deleteByUserId(userId);
    }
}

package com.WisdomMonkey.CinemaTicketBooking_Backend.controller;

import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UpdateProfileDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UserProfileDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UserSearchResultDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UserSummaryDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserProfile;
import com.WisdomMonkey.CinemaTicketBooking_Backend.security.JwtService;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserProfileService;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profiles")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class ProfilesController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDto profileDto = convertToUserProfileDto(user);
        return ResponseEntity.ok(profileDto);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDto> updateCurrentUserProfile(@RequestBody @Valid UpdateProfileDto updateDto,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        // Get current user
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Update user profile
        User updatedUser = updateUserProfile(user, updateDto);

        // Return updated profile
        UserProfileDto profileDto = convertToUserProfileDto(updatedUser);
        return ResponseEntity.ok(profileDto);
    }

    private User updateUserProfile(User user, UpdateProfileDto updateDto) {

        if (updateDto.getDisplayName() != null) {
            user.setFirstname(updateDto.getDisplayName());
        }

        User savedUser = userService.save(user);

        userProfileService.createOrUpdateProfile(savedUser, updateDto);

        return savedUser;
    }

    @GetMapping("/search")
    public ResponseEntity<UserSearchResultDto> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        // Get current user to exclude from search results
        Long currentUserId = getCurrentUserId(request);

        // Simple search by username or email (you can enhance this)
        List<User> allUsers = userService.findAll();
        List<User> filteredUsers = allUsers.stream()
                .filter(user -> !user.getId().equals(currentUserId)) // Exclude current user
                .filter(user -> user.getUsername().toLowerCase().contains(query.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(query.toLowerCase()) ||
                        user.getFirstname().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        // Simple pagination
        int start = page * size;
        int end = Math.min(start + size, filteredUsers.size());
        List<User> pagedUsers = filteredUsers.subList(start, end);

        List<UserSummaryDto> userSummaries = pagedUsers.stream()
                .map(this::convertToUserSummaryDto)
                .collect(Collectors.toList());

        UserSearchResultDto result = new UserSearchResultDto();
        result.setUsers(userSummaries);
        result.setTotalCount(filteredUsers.size());
        result.setCurrentPage(page);
        result.setTotalPages((int) Math.ceil((double) filteredUsers.size() / size));

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable String userId) {
        User user = userService.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfileDto profileDto = convertToUserProfileDto(user);
        return ResponseEntity.ok(profileDto);
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        System.out.println("=== GET CURRENT USER ID START ===");
        String token = extractTokenFromRequest(request);
        System.out.println("Extracted token: " + (token != null ? "Present (length: " + token.length() + ")" : "null"));
        String email = jwtService.extractEmail(token);
        System.out.println("Extracted email from token: " + email);
        Optional<User> userOpt = userService.findByEmail(email);
        System.out.println("User found by email: " + userOpt.isPresent());
        // User user = userService.findByEmail(email)
        // .orElseThrow(() -> new RuntimeException("User not found"));
        // return user.getId();
        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();
            System.out.println("User ID: " + userId);
            System.out.println("=== GET CURRENT USER ID END ===");
            return userId;
        } else {
            System.out.println("ERROR: No user found for email: " + email);
            System.out.println("=== GET CURRENT USER ID END ===");
            throw new RuntimeException("User not found");
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        System.out.println("=== EXTRACT TOKEN START ===");
        String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + (header != null ? "Present" : "null"));
        if (header != null && header.startsWith("Bearer ")) {
            // return header.substring(7);
            String token = header.substring(7);
            System.out.println("Token extracted successfully");
            System.out.println("=== EXTRACT TOKEN END ===");
            return token;
        }
        System.out.println("ERROR: No valid token found in header");
        System.out.println("=== EXTRACT TOKEN END ===");
        throw new RuntimeException("No valid token found");
    }

    private UserProfileDto convertToUserProfileDto(User user) {
        UserProfileDto dto = new UserProfileDto();
        dto.setId(user.getId().toString());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setDisplayName(user.getFirstname());
        dto.setAvatarUrl(user.getAvatar());

        // Get UserProfile data
        Optional<UserProfile> userProfile = userProfileService.findByUser(user);
        if (userProfile.isPresent()) {
            UserProfile profile = userProfile.get();
            dto.setBio(profile.getBio());
            dto.setIsPublic(profile.getProfileVisibility() == UserProfile.ProfileVisibility.PUBLIC);

            // Parse favorite genres
            if (profile.getFavoriteGenres() != null && !profile.getFavoriteGenres().isEmpty()) {
                dto.setFavoriteGenres(List.of(profile.getFavoriteGenres().split(",")));
            } else {
                dto.setFavoriteGenres(List.of());
            }

            dto.setMovieCount(profile.getTotalMoviesRated() != null ? profile.getTotalMoviesRated() : 0);
        } else {
            dto.setBio(null);
            dto.setIsPublic(true); // Default visibility
            dto.setMovieCount(0);
            dto.setFavoriteGenres(List.of());
        }

        dto.setJoinedDate(user.getCreatedAt().toString());
        dto.setLastActive(user.getLastLogin() != null ? user.getLastLogin().toString() : null);

        return dto;
    }

    private UserSummaryDto convertToUserSummaryDto(User user) {
        UserSummaryDto dto = new UserSummaryDto();
        dto.setId(user.getId().toString());
        dto.setUsername(user.getUsername());
        dto.setDisplayName(user.getFirstname());
        dto.setAvatarUrl(user.getAvatar());
        dto.setIsOnline(user.isActive());

        return dto;
    }
}

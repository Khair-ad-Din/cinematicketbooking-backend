package com.WisdomMonkey.CinemaTicketBooking_Backend.controller;

import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.FriendRequestDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.SendFriendRequestDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UserConnectionDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.dto.UserSummaryDto;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.Friendship;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserProfile;
import com.WisdomMonkey.CinemaTicketBooking_Backend.security.JwtService;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.FriendshipService;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserProfileService;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/friends")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class FriendsController {

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<UserConnectionDto>> getFriends(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        List<Friendship> friendships = friendshipService.getFriends(userId);
        List<UserConnectionDto> connections = friendships.stream()
                .map(friendship -> convertToUserConnectionDto(friendship, userId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(connections);
    }

    @GetMapping("/requests")
    public ResponseEntity<List<FriendRequestDto>> getFriendRequests(HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        List<Friendship> received = friendshipService.getReceivedRequests(userId);
        List<Friendship> sent = friendshipService.getSentRequests(userId);

        List<FriendRequestDto> allRequests = received.stream()
                .map(this::convertToFriendRequestDto)
                .collect(Collectors.toList());

        allRequests.addAll(sent.stream()
                .map(this::convertToFriendRequestDto)
                .collect(Collectors.toList()));

        return ResponseEntity.ok(allRequests);
    }

    @PostMapping("/requests")
    public ResponseEntity<FriendRequestDto> sendFriendRequest(
            @RequestBody SendFriendRequestDto dto,
            HttpServletRequest request) {
        Long fromUserId = getCurrentUserId(request);
        Long toUserId = Long.parseLong(dto.getToUserId());

        Friendship friendship = friendshipService.sendFriendRequest(fromUserId, toUserId, dto.getMessage());
        FriendRequestDto responseDto = convertToFriendRequestDto(friendship);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/requests/{requestId}/accept")
    public ResponseEntity<UserConnectionDto> acceptRequest(
            @PathVariable Long requestId,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        Friendship friendship = friendshipService.acceptFriendRequest(requestId, userId);
        UserConnectionDto connectionDto = convertToUserConnectionDto(friendship, userId);

        return ResponseEntity.ok(connectionDto);
    }

    @PutMapping("/requests/{requestId}/reject")
    public ResponseEntity<Void> rejectRequest(
            @PathVariable Long requestId,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        friendshipService.rejectFriendRequest(requestId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{friendId}")
    public ResponseEntity<Void> removeFriend(
            @PathVariable Long friendId,
            HttpServletRequest request) {
        Long userId = getCurrentUserId(request);

        friendshipService.removeFriendByUserId(friendId, userId);
        return ResponseEntity.ok().build();
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        String email = jwtService.extractEmail(token);
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        throw new RuntimeException("No valid token found");
    }

    private UserConnectionDto convertToUserConnectionDto(Friendship friendship, Long currentUserId) {
        // Determine which user is the friend
        User friend = friendship.getUser1().getId().equals(currentUserId)
                ? friendship.getUser2()
                : friendship.getUser1();

        UserSummaryDto friendSummary = new UserSummaryDto();
        friendSummary.setId(friend.getId().toString());
        friendSummary.setUsername(friend.getUsername());
        String displayName = userProfileService.findByUser(friend)
                .map(UserProfile::getDisplayName)
                .orElse(friend.getUsername());
        friendSummary.setDisplayName(displayName);
        friendSummary.setIsOnline(friend.isActive()); // Using active as online status

        UserConnectionDto dto = new UserConnectionDto();
        dto.setId(friendship.getId().toString());
        dto.setFriend(friendSummary);
        dto.setStatus("ACCEPTED");
        dto.setCreatedAt(friendship.getCreatedAt().toString());

        return dto;
    }

    private FriendRequestDto convertToFriendRequestDto(Friendship friendship) {
        UserSummaryDto fromUser = new UserSummaryDto();
        fromUser.setId(friendship.getUser1().getId().toString());
        fromUser.setUsername(friendship.getUser1().getUsername());
        String fromDisplayName = userProfileService.findByUser(friendship.getUser1())
                .map(UserProfile::getDisplayName)
                .orElse(friendship.getUser1().getUsername());
        fromUser.setDisplayName(fromDisplayName);
        String fromAvatarUrl = userProfileService.findByUser(friendship.getUser1())
                .map(UserProfile::getProfilePictureUrl)
                .orElse(null);
        fromUser.setAvatarUrl(fromAvatarUrl);
        fromUser.setIsOnline(friendship.getUser1().isActive());

        UserSummaryDto toUser = new UserSummaryDto();
        toUser.setId(friendship.getUser2().getId().toString());
        toUser.setUsername(friendship.getUser2().getUsername());
        String toDisplayName = userProfileService.findByUser(friendship.getUser2())
                .map(UserProfile::getDisplayName)
                .orElse(friendship.getUser2().getUsername());
        toUser.setDisplayName(toDisplayName);
        String toAvatarUrl = userProfileService.findByUser(friendship.getUser2())
                .map(UserProfile::getProfilePictureUrl)
                .orElse(null);
        toUser.setAvatarUrl(toAvatarUrl);
        toUser.setIsOnline(friendship.getUser2().isActive());

        FriendRequestDto dto = new FriendRequestDto();
        dto.setId(friendship.getId().toString());
        dto.setFromUser(fromUser);
        dto.setToUser(toUser);
        dto.setStatus(friendship.getStatus().toString());
        dto.setCreatedAt(friendship.getCreatedAt().toString());

        return dto;
    }
}
package com.WisdomMonkey.CinemaTicketBooking_Backend.service;

import java.util.List;
import java.util.Optional;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.Friendship;

public interface FriendshipService {

    List<Friendship> getFriends(Long userId);

    List<Friendship> getReceivedRequests(Long userId);

    List<Friendship> getSentRequests(Long userId);

    Friendship sendFriendRequest(Long fromUserId, Long toUserId, String message);

    Friendship acceptFriendRequest(Long requestId, Long userId);

    void rejectFriendRequest(Long requestId, Long userId);

    void removeFriend(Long friendshipId, Long userId);

    void removeFriendByUserId(Long friendUserId, Long currentUserId);

    Optional<Friendship> findById(Long id);

    boolean existsFriendship(Long user1Id, Long user2Id);
}
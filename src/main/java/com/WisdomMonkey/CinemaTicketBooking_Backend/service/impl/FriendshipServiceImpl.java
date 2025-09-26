package com.WisdomMonkey.CinemaTicketBooking_Backend.service.impl;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.Friendship;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.FriendshipStatus;
import com.WisdomMonkey.CinemaTicketBooking_Backend.repository.FriendshipRepository;
import com.WisdomMonkey.CinemaTicketBooking_Backend.repository.UserRepository;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FriendshipServiceImpl implements FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendshipServiceImpl(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Friendship> getFriends(Long userId) {
        return friendshipRepository.findAll().stream()
                .filter(f -> (f.getUser1().getId().equals(userId) || f.getUser2().getId().equals(userId))
                        && f.getStatus() == FriendshipStatus.ACCEPTED)
                .collect(Collectors.toList());
    }

    @Override
    public List<Friendship> getReceivedRequests(Long userId) {
        return friendshipRepository.findByUser2IdAndStatus(userId, FriendshipStatus.PENDING);
    }

    @Override
    public List<Friendship> getSentRequests(Long userId) {
        return friendshipRepository.findAll().stream()
                .filter(f -> f.getUser1().getId().equals(userId) && f.getStatus() == FriendshipStatus.PENDING)
                .collect(Collectors.toList());
    }

    @Override
    public Friendship sendFriendRequest(Long fromUserId, Long toUserId, String message) {
        // Check if friendship already exists
        if (existsFriendship(fromUserId, toUserId)) {
            throw new IllegalArgumentException("Friendship already exists or pending");
        }

        User fromUser = userRepository.findById(fromUserId)
                .orElseThrow(() -> new IllegalArgumentException("From user not found"));
        User toUser = userRepository.findById(toUserId)
                .orElseThrow(() -> new IllegalArgumentException("To user not found"));

        Friendship friendship = new Friendship();
        friendship.setUser1(fromUser);
        friendship.setUser2(toUser);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setCreatedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    @Override
    public Friendship acceptFriendRequest(Long requestId, Long userId) {
        Friendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        // Validate that userId is the recipient of the request
        if (!friendship.getUser2().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to accept this request");
        }

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalArgumentException("Request is not pending");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendship.setAcceptedAt(LocalDateTime.now());

        return friendshipRepository.save(friendship);
    }

    @Override
    public void rejectFriendRequest(Long requestId, Long userId) {
        Friendship friendship = friendshipRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Friend request not found"));

        // Validate that userId can reject this request (either sender or receiver)
        if (!friendship.getUser1().getId().equals(userId) && !friendship.getUser2().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to reject this request");
        }

        friendshipRepository.delete(friendship);
    }

    @Override
    public void removeFriend(Long friendshipId, Long userId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found"));

        // Validate that userId is part of this friendship
        if (!friendship.getUser1().getId().equals(userId) && !friendship.getUser2().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to remove this friendship");
        }

        friendshipRepository.delete(friendship);
    }

    @Override
    public Optional<Friendship> findById(Long id) {
        return friendshipRepository.findById(id);
    }

    @Override
    public boolean existsFriendship(Long user1Id, Long user2Id) {
        return friendshipRepository.existsByUser1IdAndUser2IdOrUser2IdAndUser1Id(user1Id, user2Id, user1Id, user2Id);
    }
}
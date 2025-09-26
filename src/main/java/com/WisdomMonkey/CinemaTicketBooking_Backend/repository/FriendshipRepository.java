package com.WisdomMonkey.CinemaTicketBooking_Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.Friendship;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.FriendshipStatus;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUser1IdOrUser2IdAndStatus(Long userId1, Long userId2, FriendshipStatus status);

    List<Friendship> findByUser2IdAndStatus(Long userId, FriendshipStatus status);

    Optional<Friendship> findByUser1IdAndUser2IdOrUser2IdAndUser1Id(Long user1Id, Long user2Id, Long user2Id2,
            Long user1Id2);

    boolean existsByUser1IdAndUser2IdOrUser2IdAndUser1Id(Long user1Id, Long user2Id, Long user2Id2, Long user1Id2);
}

package com.WisdomMonkey.CinemaTicketBooking_Backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.Friendship;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.FriendshipStatus;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query("SELECT f FROM Friendship f WHERE (f.user1.id = :userId OR f.user2.id = :userId) AND f.status = :status")
    List<Friendship> findFriendshipsByUserIdAndStatus(@Param("userId") Long userId, @Param("status") FriendshipStatus status);

    List<Friendship> findByUser2IdAndStatus(Long userId, FriendshipStatus status);

    List<Friendship> findByUser1IdAndStatus(Long userId, FriendshipStatus status);

    Optional<Friendship> findByUser1IdAndUser2IdOrUser2IdAndUser1Id(Long user1Id, Long user2Id, Long user2Id2,
            Long user1Id2);

    boolean existsByUser1IdAndUser2IdOrUser2IdAndUser1Id(Long user1Id, Long user2Id, Long user2Id2, Long user1Id2);
}

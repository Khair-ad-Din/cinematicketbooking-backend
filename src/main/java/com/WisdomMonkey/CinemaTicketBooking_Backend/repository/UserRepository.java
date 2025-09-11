package com.WisdomMonkey.CinemaTicketBooking_Backend.repository;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByActive(Boolean status);
    Long countByActive(Boolean status);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}

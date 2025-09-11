package com.WisdomMonkey.CinemaTicketBooking_Backend.service;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByActive(Boolean status);

    User save(User user);

    boolean deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void updateLastAccess(String username);

    long countAll();

    long countByActive(boolean status);
}

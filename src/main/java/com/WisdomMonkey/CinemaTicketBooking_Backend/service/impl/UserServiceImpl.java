package com.WisdomMonkey.CinemaTicketBooking_Backend.service.impl;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.repository.UserRepository;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByActive(Boolean status) {
        return userRepository.findByActive(status);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean deleteById(Long id) {
        userRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void updateLastAccess(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }

    @Override
    public long countAll() {
        return userRepository.count();
    }

    @Override
    public long countByActive(boolean status) {
        return userRepository.countByActive(status);
    }
}

package com.WisdomMonkey.CinemaTicketBooking_Backend.repository;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserPreference;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.PreferenceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

    List<UserPreference> findByUserAndPreferenceType(User user, PreferenceType preferenceType);

    List<UserPreference> findByUser(User user);

    Optional<UserPreference> findByUserAndPreferenceTypeAndPreferenceValue(User user, PreferenceType preferenceType,
            String preferenceValue);

    void deleteByUserAndPreferenceTypeAndPreferenceValue(User user, PreferenceType preferenceType,
            String preferenceValue);

    void deleteByUserAndPreferenceType(User user, PreferenceType preferenceType);
}
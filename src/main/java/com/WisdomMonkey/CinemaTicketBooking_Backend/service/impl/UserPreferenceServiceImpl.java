package com.WisdomMonkey.CinemaTicketBooking_Backend.service.impl;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserPreference;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.PreferenceType;
import com.WisdomMonkey.CinemaTicketBooking_Backend.repository.UserPreferenceRepository;
import com.WisdomMonkey.CinemaTicketBooking_Backend.service.UserPreferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserPreferenceServiceImpl implements UserPreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    @Override
    public List<UserPreference> getPreferencesByType(User user, PreferenceType type) {
        return userPreferenceRepository.findByUserAndPreferenceType(user, type);
    }

    @Override
    public UserPreference savePreference(User user, PreferenceType type, String value, BigDecimal weight) {
        UserPreference preference = userPreferenceRepository
                .findByUserAndPreferenceTypeAndPreferenceValue(user, type, value)
                .orElse(new UserPreference());

        preference.setUser(user);
        preference.setPreferenceType(type);
        preference.setPreferenceValue(value);
        preference.setWeightScore(weight != null ? weight : BigDecimal.valueOf(0.5));

        return userPreferenceRepository.save(preference);
    }

    @Override
    public void updateGenrePreferences(User user, List<String> genres) {
        // Delete existing genre preferences
        deleteAllPreferencesByType(user, PreferenceType.GENRE);

        // Create new genre preferences
        if (genres != null && !genres.isEmpty()) {
            for (String genre : genres) {
                savePreference(user, PreferenceType.GENRE, genre, BigDecimal.valueOf(0.5));
            }
        }
    }

    @Override
    public List<String> getFavoriteGenres(User user) {
        return getPreferencesByType(user, PreferenceType.GENRE)
                .stream()
                .map(UserPreference::getPreferenceValue)
                .collect(Collectors.toList());
    }

    @Override
    public void deletePreference(User user, PreferenceType type, String value) {
        userPreferenceRepository.deleteByUserAndPreferenceTypeAndPreferenceValue(user, type, value);
    }

    @Override
    public void deleteAllPreferencesByType(User user, PreferenceType type) {
        userPreferenceRepository.deleteByUserAndPreferenceType(user, type);
    }
}
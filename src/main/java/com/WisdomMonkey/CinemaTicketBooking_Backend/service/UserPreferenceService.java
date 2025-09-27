package com.WisdomMonkey.CinemaTicketBooking_Backend.service;

import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.User;
import com.WisdomMonkey.CinemaTicketBooking_Backend.entity.UserPreference;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.PreferenceType;

import java.math.BigDecimal;
import java.util.List;

public interface UserPreferenceService {

    List<UserPreference> getPreferencesByType(User user, PreferenceType type);

    UserPreference savePreference(User user, PreferenceType type, String value, BigDecimal weight);

    void updateGenrePreferences(User user, List<String> genres);

    List<String> getFavoriteGenres(User user);

    void deletePreference(User user, PreferenceType type, String value);

    void deleteAllPreferencesByType(User user, PreferenceType type);
}
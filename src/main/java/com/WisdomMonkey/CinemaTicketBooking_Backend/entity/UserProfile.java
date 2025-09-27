package com.WisdomMonkey.CinemaTicketBooking_Backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * UserProfile Entity - Extended profile information for users
 *
 * This entity stores additional profile information separate from core User
 * authentication data.
 * Separating profile data from user authentication follows best practices for:
 * - Security: Core auth data remains minimal and secure
 * - Performance: Profile data can be loaded separately when needed
 * - Scalability: Profile data can be cached independently
 * - Privacy: Users can update profile info without affecting auth
 *
 * Profile Information Categories:
 * - Personal Details: bio, age, location, profile picture
 * - Movie Preferences: favorite genres, viewing habits
 * - Social Features: privacy settings, friend visibility
 * - App Behavior: onboarding status, tutorial completion
 *
 * One-to-One Relationship:
 * - Each User has exactly one UserProfile
 * - UserProfile cannot exist without a User
 * - Foreign key relationship with cascade options
 */
@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // Personal Information
    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "favorite_genres", length = 200)
    private String favoriteGenres;

    @Column(name = "preferred_language", length = 10)
    private String preferredLanguage;

    @Min(1)
    @Max(5)
    @Column(name = "min_rating_threshold")
    private Integer minRatingThreshold;

    @Column(name = "profile_visibility")
    @Enumerated(EnumType.STRING)
    private ProfileVisibility profileVisibility = ProfileVisibility.FRIENDS_ONLY;

    @Column(name = "allow_friend_requests")
    private Boolean allowFriendRequests = true;

    @Column(name = "show_watching_activity")
    private Boolean showWatchingActivity = true;

    @Column(name = "onboarding_completed")
    private Boolean onboardingCompleted = false;

    @Column(name = "tutorial_completed")
    private Boolean tutorialCompleted = false;

    @Column(name = "swipe_tutorial_completed")
    private Boolean swipeTutorialCompleted = false;

    @Column(name = "total_movies_rated")
    private Integer totalMoviesRated = 0;

    @Column(name = "total_movies_liked")
    private Integer totalMoviesLiked = 0;

    @Column(name = "total_movies_disliked")
    private Integer totalMoviesDisliked = 0;

    @Column(name = "total_friends")
    private Integer totalFriends = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum ProfileVisibility {
        PUBLIC,
        FRIENDS_ONLY,
        PRIVATE
    }

    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean isFullyOnboarded() {
        return Boolean.TRUE.equals(onboardingCompleted) &&
                Boolean.TRUE.equals(tutorialCompleted) &&
                Boolean.TRUE.equals(swipeTutorialCompleted);
    }

    public int getTotalMovieInteractions() {
        return (totalMoviesLiked != null ? totalMoviesLiked : 0) +
                (totalMoviesDisliked != null ? totalMoviesDisliked : 0);
    }
}
package com.WisdomMonkey.CinemaTicketBooking_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SwipeSession Entity - Tracks user movie discovery sessions
 *
 * This entity represents a single movie discovery session where a user swipes through movies.
 * Each session tracks the user's swiping behavior, preferences, and outcomes to improve
 * the recommendation algorithm and provide analytics.
 *
 * Why Track Swipe Sessions?
 * - Analytics: Understand user engagement patterns and session duration
 * - Algorithm Improvement: Track which movie combinations work well together
 * - User Experience: Resume sessions, avoid showing same movies repeatedly
 * - Performance Metrics: Monitor session quality and user satisfaction
 * - A/B Testing: Compare different recommendation strategies
 *
 * Session Lifecycle:
 * 1. STARTED: User begins swiping, movies are loaded
 * 2. ACTIVE: User is actively swiping through movies
 * 3. PAUSED: Session temporarily paused (app backgrounded, phone call, etc.)
 * 4. COMPLETED: User finished session normally (found movies or completed queue)
 * 5. ABANDONED: User left without completing (session timeout, app closed)
 *
 * Business Value:
 * - Track user engagement and retention
 * - Identify optimal session length and movie count
 * - Measure recommendation quality and conversion rates
 * - Detect user patterns for personalization
 */
@Entity
@Table(name = "swipe_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwipeSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "session_token", unique = true, length = 100)
    private String sessionToken; // Unique identifier for frontend tracking

    @Column(name = "device_type", length = 20)
    private String deviceType;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.STARTED;

    @CreationTimestamp
    @Column(name = "started_at", updatable = false)
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "target_genre", length = 50)
    private String targetGenre;

    @Column(name = "min_rating")
    private Double minRating;

    @Column(name = "max_year")
    private Integer maxYear;

    @Column(name = "min_year")
    private Integer minYear;

    @Column(name = "language_filter", length = 10)
    private String languageFilter;

    @Column(name = "total_movies_shown")
    private Integer totalMoviesShown = 0;

    @Column(name = "total_movies_liked")
    private Integer totalMoviesLiked = 0;

    @Column(name = "total_movies_disliked")
    private Integer totalMoviesDisliked = 0;

    @Column(name = "total_movies_saved")
    private Integer totalMoviesSaved = 0;

    @Column(name = "total_movies_skipped")
    private Integer totalMoviesSkipped = 0;

    @Column(name = "engagement_score")
    private Double engagementScore;

    @Column(name = "recommendation_accuracy")
    private Double recommendationAccuracy;

    @Column(name = "session_satisfaction")
    private Integer sessionSatisfaction;

    @Column(name = "recommendation_strategy", length = 50)
    private String recommendationStrategy;

    @Column(name = "is_new_user_session")
    private Boolean isNewUserSession = false;

    @Column(name = "session_notes", length = 500)
    private String sessionNotes;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum SessionStatus {
        STARTED,    // Session created, ready to show movies
        ACTIVE,     // User actively swiping
        PAUSED,     // Temporarily paused (app backgrounded)
        COMPLETED,  // Session finished successfully
        ABANDONED,  // Session ended without completion
        EXPIRED     // Session timed out
    }

    @OneToMany(mappedBy = "swipeSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieInteraction> interactions;

    public void calculateDuration() {
        if (startedAt != null && endedAt != null) {
            this.durationMinutes = (int) java.time.Duration.between(startedAt, endedAt).toMinutes();
        }
    }

    public void calculateEngagementScore() {
        if (totalMoviesShown == null || totalMoviesShown == 0) {
            this.engagementScore = 0.0;
            return;
        }

        int positiveInteractions = (totalMoviesLiked != null ? totalMoviesLiked : 0) +
                                 (totalMoviesSaved != null ? totalMoviesSaved : 0);
        int negativeInteractions = (totalMoviesSkipped != null ? totalMoviesSkipped : 0);

        // Score based on positive interactions vs total movies shown
        this.engagementScore = (double) positiveInteractions / totalMoviesShown;
    }

    /**
     * Get session conversion rate (liked/saved vs total shown)
     * Useful metric for measuring recommendation quality
     */
    public double getConversionRate() {
        if (totalMoviesShown == null || totalMoviesShown == 0) {
            return 0.0;
        }

        int conversions = (totalMoviesLiked != null ? totalMoviesLiked : 0) +
                         (totalMoviesSaved != null ? totalMoviesSaved : 0);
        return (double) conversions / totalMoviesShown;
    }

    public boolean isActive() {
        return status == SessionStatus.STARTED || status == SessionStatus.ACTIVE || status == SessionStatus.PAUSED;
    }

    public void completeSession() {
        this.status = SessionStatus.COMPLETED;
        this.endedAt = LocalDateTime.now();
        calculateDuration();
        calculateEngagementScore();
    }

    public void abandonSession() {
        this.status = SessionStatus.ABANDONED;
        this.endedAt = LocalDateTime.now();
        calculateDuration();
        calculateEngagementScore();
    }

    public void updateLastActivity() {
        this.lastActivityAt = LocalDateTime.now();
        if (this.status == SessionStatus.STARTED) {
            this.status = SessionStatus.ACTIVE;
        }
    }
}
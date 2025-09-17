package com.WisdomMonkey.CinemaTicketBooking_Backend.entity;

import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.InteractionType;
import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.SwipeDirection;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "movie_interactions",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "movie_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Enumerated(EnumType.STRING)
    @Column(name = "interaction_type", nullable = false)
    private InteractionType interactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "swipe_direction", nullable = false)
    private SwipeDirection swipeDirection;

    @Column(name = "is_saved")
    private Boolean isSaved = false;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "swipe_timestamp", nullable = false)
    @CreationTimestamp
    private LocalDateTime swipeTimestamp;

    @Column(name = "device_type", length = 20)
    private String deviceType;

    @Column(name = "app_version", length = 20)
    private String appVersion;
}
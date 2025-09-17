package com.WisdomMonkey.CinemaTicketBooking_Backend.entity;

import com.WisdomMonkey.CinemaTicketBooking_Backend.enums.PreferenceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "preference_type", "preference_value"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "preference_type", nullable = false)
    private PreferenceType preferenceType;

    @Column(name = "preference_value", nullable = false, length = 255)
    private String preferenceValue;

    @Column(name = "weight_score", precision = 5, scale = 4, nullable = false)
    private BigDecimal weightScore = BigDecimal.valueOf(0.5);

    @Column(name = "interaction_count")
    private Integer interactionCount = 0;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
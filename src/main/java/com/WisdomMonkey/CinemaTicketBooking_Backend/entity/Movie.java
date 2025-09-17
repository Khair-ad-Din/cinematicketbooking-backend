package com.WisdomMonkey.CinemaTicketBooking_Backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tmdb_id", nullable = false, unique = true)
    private Integer tmdbId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "original_title", length = 255)
    private String originalTitle;

    @Lob
    private String overview;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "poster_path", length = 500)
    private String posterPath;

    @Column(name = "backdrop_path", length = 500)
    private String backdropPath;

    @Column(name = "vote_average", precision = 3, scale = 1)
    private BigDecimal voteAverage;

    @Column(name = "vote_count")
    private Integer voteCount;

    @Column(length = 500)
    private String genres;

    @Column(name = "runtime_minutes")
    private Integer runtimeMinutes;

    @Column(name = "original_language", length = 10)
    private String originalLanguage;

    @Column(name = "adult_content")
    private Boolean adultContent = false;

    @Column(name = "budget")
    private Long budget;

    @Column(name = "revenue")
    private Long revenue;

    @Column(name = "director", length = 255)
    private String director;

    @Column(name = "cast_members", length = 1000)
    private String castMembers;

    @Column(name = "popularity", precision = 8, scale = 3)
    private BigDecimal popularity;

    @Column(name = "imdb_id", length = 20)
    private String imdbId;

    @Column(name = "streaming_platforms", length = 500)
    private String streamingPlatforms;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
}
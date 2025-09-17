-- Create saved_movies table (watchlist)
CREATE TABLE saved_movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    is_watched BOOLEAN DEFAULT FALSE,
    user_rating DECIMAL(2,1) NULL,
    watch_priority INT DEFAULT 1, -- 1=low, 2=medium, 3=high
    notes VARCHAR(500) NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    watched_at TIMESTAMP NULL,
    CONSTRAINT fk_saved_movies_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_saved_movies_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Create unique constraint and indexes for saved_movies
ALTER TABLE saved_movies ADD CONSTRAINT unique_saved_movie UNIQUE (user_id, movie_id);
CREATE INDEX idx_saved_movies_user ON saved_movies(user_id, is_watched, watch_priority);
CREATE INDEX idx_saved_movies_saved ON saved_movies(saved_at);
CREATE INDEX idx_saved_movies_rating ON saved_movies(user_rating);
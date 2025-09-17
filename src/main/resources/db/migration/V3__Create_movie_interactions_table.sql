-- Create movie_interactions table
CREATE TABLE movie_interactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    movie_id BIGINT NOT NULL,
    interaction_type VARCHAR(20) NOT NULL,
    swipe_direction VARCHAR(10) NOT NULL,
    is_saved BOOLEAN DEFAULT FALSE,
    session_id VARCHAR(100),
    swipe_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    device_type VARCHAR(20),
    app_version VARCHAR(20),
    CONSTRAINT fk_interactions_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_interactions_movie FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);

-- Create unique constraint and indexes for movie_interactions
ALTER TABLE movie_interactions ADD CONSTRAINT unique_user_movie UNIQUE (user_id, movie_id);
CREATE INDEX idx_interactions_user ON movie_interactions(user_id, swipe_timestamp);
CREATE INDEX idx_interactions_movie ON movie_interactions(movie_id, interaction_type);
CREATE INDEX idx_interactions_session ON movie_interactions(session_id);
CREATE INDEX idx_interactions_timestamp ON movie_interactions(swipe_timestamp);
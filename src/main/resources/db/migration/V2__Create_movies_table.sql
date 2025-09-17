-- Create movies table
CREATE TABLE movies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tmdb_id INT NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    original_title VARCHAR(255),
    overview TEXT,
    release_date DATE,
    poster_path VARCHAR(500),
    backdrop_path VARCHAR(500),
    vote_average DECIMAL(3,1),
    vote_count INT,
    genres VARCHAR(500),
    runtime_minutes INT,
    original_language VARCHAR(10),
    adult_content BOOLEAN DEFAULT FALSE,
    budget BIGINT,
    revenue BIGINT,
    director VARCHAR(255),
    cast_members VARCHAR(1000),
    popularity DECIMAL(8,3),
    imdb_id VARCHAR(20),
    streaming_platforms VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for movies table
CREATE INDEX idx_movies_tmdb_id ON movies(tmdb_id);
CREATE INDEX idx_movies_title ON movies(title);
CREATE INDEX idx_movies_release_date ON movies(release_date);
CREATE INDEX idx_movies_vote_average ON movies(vote_average);
CREATE INDEX idx_movies_popularity ON movies(popularity);
CREATE INDEX idx_movies_genres ON movies(genres);
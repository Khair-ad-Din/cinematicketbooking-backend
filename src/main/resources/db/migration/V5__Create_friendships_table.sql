-- Create friendships table
CREATE TABLE friendships (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user1_id BIGINT NOT NULL,
    user2_id BIGINT NOT NULL,
    status VARCHAR(10) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP NULL,
    blocked_at TIMESTAMP NULL,
    blocked_by_user_id BIGINT NULL,
    CONSTRAINT fk_friendship_user1 FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_friendship_user2 FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create unique constraint and indexes for friendships
ALTER TABLE friendships ADD CONSTRAINT unique_friendship UNIQUE (user1_id, user2_id);
CREATE INDEX idx_friendships_user1 ON friendships(user1_id, status);
CREATE INDEX idx_friendships_user2 ON friendships(user2_id, status);
CREATE INDEX idx_friendships_status ON friendships(status);
CREATE INDEX idx_friendships_created ON friendships(created_at);
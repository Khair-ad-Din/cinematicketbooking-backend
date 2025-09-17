-- Create user_preferences table
CREATE TABLE user_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    preference_type VARCHAR(30) NOT NULL,
    preference_value VARCHAR(255) NOT NULL,
    weight_score DECIMAL(5,4) NOT NULL DEFAULT 0.5000,
    interaction_count INT DEFAULT 0,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_preferences_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create unique constraint and indexes for user_preferences
ALTER TABLE user_preferences ADD CONSTRAINT unique_user_preference UNIQUE (user_id, preference_type, preference_value);
CREATE INDEX idx_preferences_user ON user_preferences(user_id, preference_type);
CREATE INDEX idx_preferences_weight ON user_preferences(weight_score);
CREATE INDEX idx_preferences_updated ON user_preferences(updated_at);
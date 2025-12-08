CREATE TABLE activation_tokens (
                                   user_id UUID PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
                                   token VARCHAR(255) NOT NULL UNIQUE,
                                   created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

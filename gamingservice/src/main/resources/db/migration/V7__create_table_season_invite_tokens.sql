CREATE TABLE session_invites (
                                 id UUID PRIMARY KEY,
                                 session_id UUID NOT NULL,
                                 invited_user_id UUID NOT NULL,
                                 token VARCHAR(255) UNIQUE NOT NULL,
                                 used BOOLEAN DEFAULT FALSE,
                                 expiry_date TIMESTAMP
);
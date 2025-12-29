CREATE TABLE participant(
    participant_id UUID PRIMARY KEY,
    user_id UUID NOT NULL ,
    session_id UUID NOT NULL ,
    email VARCHAR(120) NOT NULL ,
    CONSTRAINT unique_user_per_session UNIQUE (user_id, session_id)
);
CREATE TABLE sessions (
                          session_id UUID PRIMARY KEY,
                          creator_id UUID NOT NULL,
                          session_name VARCHAR(100) NOT NULL,
                          game_name VARCHAR(100) NOT NULL,
                          max_players INT NOT NULL,
                          session_type VARCHAR(20) NOT NULL,
                          start_of_session TIMESTAMP NOT NULL,
                          description VARCHAR(300) NOT NULL,
                          session_status VARCHAR(20) NOT NULL
);
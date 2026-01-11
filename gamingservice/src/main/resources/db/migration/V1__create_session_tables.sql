CREATE TABLE sessions (
                          session_id UUID PRIMARY KEY,
                          creator_id UUID NOT NULL,
                          owner_username VARCHAR(100) NOT NULL default 'gamer_one',
                          session_name VARCHAR(100) NOT NULL UNIQUE,
                          game_name VARCHAR(100) NOT NULL,
                          max_players INT NOT NULL,
                          number_of_joined_players INT NOT NULL DEFAULT 0,
                          session_type VARCHAR(20) NOT NULL,
                          start_of_session TIMESTAMP NOT NULL,
                          description VARCHAR(300) NOT NULL,
                          session_status VARCHAR(20) NOT NULL,
                          reminder60sent BOOLEAN NOT NULL DEFAULT FALSE
)
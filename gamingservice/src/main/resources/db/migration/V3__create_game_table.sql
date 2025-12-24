CREATE TABLE game (
    game_id UUID PRIMARY KEY,
    game_name VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    game_type VARCHAR(100) NOT NULL
)
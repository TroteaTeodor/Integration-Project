-- Active: 1733749329420@@127.0.0.1@5432@ascii29@public
-- Create the players table
-- Create the players table if it doesn't already exist
CREATE TABLE IF NOT EXISTS players (
    id SERIAL PRIMARY KEY,
    player_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,  -- Password should be stored securely (hashed)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the games table if it doesn't already exist
CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    player_id INTEGER REFERENCES players(id) ON DELETE CASCADE,  -- Foreign key reference to players table
    board TEXT NOT NULL,
    score INTEGER NOT NULL,
    energy INTEGER NOT NULL,
    bombs INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from games
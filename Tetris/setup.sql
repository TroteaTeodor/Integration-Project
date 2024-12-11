-- Active: 1733749329420@@127.0.0.1@5432@ascii29@public
-- Create the players table
-- Create the players table if it doesn't already exist
DROP Table if EXISTS players, games;

CREATE TABLE IF NOT EXISTS players (
    id SERIAL PRIMARY KEY,
    player_name VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    player_id INT REFERENCES players(id),
    board TEXT,
    score INT,
    energy INT,
    bombs INT,
    duration BIGINT,
    is_ended BOOLEAN DEFAULT FALSE
);

 -- test
select * from games;
SELECT * from players;
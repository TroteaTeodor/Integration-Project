CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    player_id INT REFERENCES players (id),
    board TEXT,
    score INT,
    energy INT,
    bombs INT,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP DEFAULT NULL,
    is_ended BOOLEAN DEFAULT FALSE
);
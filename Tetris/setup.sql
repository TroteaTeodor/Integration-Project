Drop table games,player,game_session;

CREATE TABLE games (
    id SERIAL PRIMARY KEY,
    board TEXT NOT NULL,
    score INTEGER NOT NULL,
    energy INTEGER NOT NULL,
    bombs INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE player (
	player_name TEXT NOT NULL,
	player_password TEXT NOT NULL
	
);

CREATE TABLE game_session (
	id SERIAL PRIMARY KEY,
	board TEXT NOT NULL,
	finished BOOLEAN NOT NULL
);
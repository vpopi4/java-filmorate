CREATE TABLE IF NOT EXISTS mpa_ratings (
    id INTEGER PRIMARY KEY,
    name VARCHAR(5) NOT NULL,
    description VARCHAR
);

CREATE TABLE IF NOT EXISTS genres (
    id INTEGER PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    login VARCHAR NOT NULL UNIQUE,
    name VARCHAR,
    birthday DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS films (
    id INTEGER PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR(200),
    release_date DATE NOT NULL,
    duration_in_minutes INTEGER NOT NULL,
    mpa_rating_id INTEGER,
    FOREIGN KEY (mpa_rating_id) REFERENCES mpa_ratings(id)
);

CREATE TABLE IF NOT EXISTS film_genres (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendships (
    user_id_from INTEGER NOT NULL,
    user_id_to INTEGER NOT NULL,
    is_accepted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id_from, user_id_to),
    FOREIGN KEY (user_id_from) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id_to) REFERENCES users(id) ON DELETE CASCADE
);

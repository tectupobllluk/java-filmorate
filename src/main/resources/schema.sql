drop table IF EXISTS
    mpa,
    reviews,
    useful_review,
    users,
    films,
    genres,
    film_genre,
    friends,
    likes;

create TABLE mpa (
    mpa_id INTEGER PRIMARY KEY,
    mpa_name VARCHAR NOT NULL
);

create TABLE users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    login VARCHAR NOT NULL UNIQUE,
    name VARCHAR,
    birthday DATE NOT NULL
);

create TABLE films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER NOT NULL,
    CONSTRAINT fk_film_mpa FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id)
);

create TABLE IF NOT EXISTS genres (
    genre_id INTEGER PRIMARY KEY,
    genre_name VARCHAR NOT NULL
);

create TABLE IF NOT EXISTS film_genre (
    film_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    CONSTRAINT fk_genre_films FOREIGN KEY (film_id)
    REFERENCES films (film_id) ON delete CASCADE,
    CONSTRAINT fk_film_genres FOREIGN KEY (genre_id)
    REFERENCES genres (genre_id) ON delete CASCADE
);

create TABLE IF NOT EXISTS friends (
    user_id INTEGER NOT NULL,
    friend_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id)
    REFERENCES users (user_id) ON delete CASCADE,
    CONSTRAINT fk_friend FOREIGN KEY (friend_id)
    REFERENCES users (user_id) ON delete CASCADE
);

create TABLE IF NOT EXISTS likes (
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, user_id),
    CONSTRAINT fk_film_like FOREIGN KEY (film_id)
    REFERENCES films (film_id) ON delete CASCADE,
    CONSTRAINT fk_user_like FOREIGN KEY (user_id)
    REFERENCES users (user_id) ON delete CASCADE
);

create TABLE IF NOT EXISTS reviews (
    review_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content_review TEXT NOT NULL,
    is_positive BOOLEAN NOT NULL,
    film_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    CONSTRAINT fk_film_review FOREIGN KEY (film_id)
    REFERENCES films (film_id) ON delete CASCADE,
    CONSTRAINT fk_user_review FOREIGN KEY (user_id)
    REFERENCES users (user_id) ON delete CASCADE
);

create TABLE IF NOT EXISTS useful_review (
    review_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    useful INTEGER NOT NULL,
    PRIMARY KEY (review_id, user_id),
    CONSTRAINT fk_review_useful FOREIGN KEY (review_id)
    REFERENCES reviews (review_id) ON delete CASCADE,
    CONSTRAINT fk_user_useful FOREIGN KEY (user_id)
    REFERENCES users (user_id) ON delete CASCADE
);
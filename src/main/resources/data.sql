INSERT INTO mpa (mpa_id, mpa_name) VALUES
    (1, 'G'),
    (2, 'PG'),
    (3, 'PG-13'),
    (4, 'R'),
    (5, 'NC-17');

INSERT INTO genres (genre_id, genre_name) VALUES
    (1, 'Комедия'),
    (2, 'Драма'),
    (3, 'Мультфильм'),
    (4, 'Триллер'),
    (5, 'Документальный'),
    (6, 'Боевик');

INSERT INTO event_types (type_id, type_name) VALUES
    (1, 'LIKE'),
    (2, 'REVIEW'),
    (3, 'FRIEND');

INSERT INTO operation_types (operation_id, operation_name) VALUES
    (1, 'REMOVE'),
    (2, 'ADD'),
    (3, 'UPDATE');

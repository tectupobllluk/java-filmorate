package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DbGenreRepository implements GenreRepository {
    private final JdbcOperations jdbcTemplate;

    @Override
    public Genre getGenre(int genreId) {
        final String sqlQuery = "SELECT * " +
                "FROM genres " +
                "WHERE genre_id = ?;";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, new GenreRowMapper(), genreId);
        if (genres.size() == 0) {
            throw new NotFoundException("Unknown Genre with " + genreId + " id!");
        }
        return genres.get(0);
    }

    @Override
    public List<Genre> getAllGenres() {
        final String sqlQuery = "SELECT * " +
                "FROM genres;";
        return jdbcTemplate.query(sqlQuery, new GenreRowMapper());
    }

    @Override
    public List<Genre> loadFilmGenre(Long filmId) {
        final String sqlQuery = "SELECT g.* " +
                "FROM film_genre AS fg " +
                "JOIN genres AS g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id = ?;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name")), filmId);
    }

    @Override
    public void insertGenres(Film film) {
        for (Genre genre : film.getGenres()) {
            final String sqlQuery = "MERGE INTO film_genre AS t " +
                    "USING (SELECT CAST(? AS INTEGER), CAST(? AS INTEGER)) AS s (film_id, genre_id) " +
                    "ON (t.film_id = s.film_id) AND (t.genre_id = s.genre_id) " +
                    "WHEN NOT MATCHED THEN " +
                    "INSERT (film_id, genre_id) " +
                    "VALUES (s.film_id, s.genre_id);";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    @Override
    public void removeFilmGenres(Film film) {
        final String sqlQuery = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    private static class GenreRowMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(rs.getString("genre_name"))
                    .build();
        }
    }
}

package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbDirectorRepository implements DirectorRepository {
    private final JdbcOperations jdbcTemplate;

    @Override
    public List<Director> loadFilmDirector(Long filmId) {
        final String sqlQuery = "SELECT d.* " +
                "FROM film_directors AS fd " +
                "JOIN directors AS d ON fd.director_id = d.director_id " +
                "WHERE fd.film_id = ?;";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new Director(rs.getLong("director_id"),
                rs.getString("director_name")), filmId);
    }

    @Override
    public Optional<Director> getDirector(Long id) {
        final String sqlQuery = "SELECT * " +
                "FROM directors " +
                "WHERE director_id = ?;";
        final List<Director> directors = jdbcTemplate.query(sqlQuery, new DirectorRowMapper(), id);
        if (directors.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(directors.get(0));
    }

    @Override
    public List<Director> getAllDirectors() {
        final String sqlQuery = "SELECT * " +
                "FROM directors;";
        return jdbcTemplate.query(sqlQuery, new DirectorRowMapper());
    }

    @Override
    public Director createDirector(Director director) {
        String sqlQuery = "INSERT INTO directors (director_name) " +
                "VALUES (?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"director_id"});
            stmt.setString(1, director.getName());
            return stmt;
        }, keyHolder);
        return getDirector(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new NotFoundException("Director not found with id = " + director.getId()));
    }

    @Override
    public Director updateDirector(Director director) {
        String sqlQuery = "UPDATE directors SET director_name = ? " +
                "WHERE director_id = ?;";
        int rowAffected = jdbcTemplate.update(sqlQuery, director.getName(), director.getId());
        if (rowAffected == 0) {
            throw new NotFoundException("Unknown director with " + director.getId() + " id!");
        }
        return getDirector(director.getId())
                .orElseThrow(() -> new NotFoundException("Director not found with id = " + director.getId()));
    }

    @Override
    public void deleteDirector(Long id) {
        final String sqlQuery = "DELETE FROM directors " +
                "WHERE director_id = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void insertDirectors(Film film) {
        for (Director director : film.getDirectors()) {
            final String sqlQuery = "MERGE INTO film_directors AS t " +
                    "USING (SELECT CAST(? AS INTEGER), CAST(? AS INTEGER)) AS s (film_id, director_id) " +
                    "ON (t.film_id = s.film_id) AND (t.director_id = s.director_id) " +
                    "WHEN NOT MATCHED THEN " +
                    "INSERT (film_id, director_id) " +
                    "VALUES (s.film_id, s.director_id);";
            jdbcTemplate.update(sqlQuery, film.getId(), director.getId());
        }
    }

    @Override
    public void removeFilmDirectors(Film film) {
        final String sqlQuery = "DELETE FROM film_directors WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    private static class DirectorRowMapper implements RowMapper<Director> {
        @Override
        public Director mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Director.builder()
                    .id(rs.getLong("director_id"))
                    .name(rs.getString("director_name"))
                    .build();
        }
    }
}

package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Primary
@RequiredArgsConstructor
public class DbFilmRepository implements FilmRepository {

    private final JdbcOperations jdbcTemplate;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    @Override
    public Film saveFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setObject(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        genreRepository.insertGenres(film);
        return getFilm(Objects.requireNonNull(keyHolder.getKey()).longValue())
                .orElseThrow(() -> new NotFoundException("Film not found with id = " +
                        Objects.requireNonNull(keyHolder.getKey()).longValue()));
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?;";
        int rowAffected = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (rowAffected == 0) {
            throw new NotFoundException("Unknown film with " + film.getId() + " id!");
        }
        genreRepository.removeFilmGenres(film);
        if (film.getGenres().size() > 0) {
            genreRepository.insertGenres(film);
        }
        return getFilm(film.getId())
                .orElseThrow(() -> new NotFoundException("Film not found with id = " + film.getId()));
    }

    @Override
    public List<Film> getAllFilms() {
        final String sqlQuery = "SELECT * " +
                "FROM films;";
        return jdbcTemplate.query(sqlQuery, new FilmRowMapper());
    }

    @Override
    public Optional<Film> getFilm(long filmId) {
        final String sqlQuery = "SELECT * " +
                "FROM films " +
                "WHERE film_id = ?;";
        final List<Film> films = jdbcTemplate.query(sqlQuery, new FilmRowMapper(), filmId);
        if (films.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(films.get(0));
    }

    @Override
    public void addLike(Film film, User user) {
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        final String sqlQuery = "SELECT f.* " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(l.user_id) DESC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), count);
    }

    @Override
    public List<Film> searchFilms(String query, String fields) {
        if (query.isEmpty()) {
            return getAllFilms();
        }

        String[] parameters = fields.split(",");
        String sqlDirectorName = "select f.film_id, " +
                "f.name, " +
                "f.mpa_id, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_name, " +
                "count(l.user_id) as flike " +
                "from films as f " +
                "inner join film_directors as fd on f.film_id = fd.film_id " +
                "inner join directors as d on d.director_id = fd.director_id " +
                "left join mpa as m on f.mpa_id = m.mpa_id " +
                "left join likes as l on f.film_id = l.film_id " +
                "where upper(d.name) like concat('%', upper(?),  '%') " +
                "group by f.film_id " +
                "order by f.film_id desc ";

        String sqlFilmName = "select f.film_id, " +
                "f.name, " +
                "f.mpa_id, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_name, " +
                "count(l.user_id) as flike " +
                "from films as f " +
                "left join mpa as m on f.mpa_id = m.mpa_id " +
                "left join likes as l on f.film_id = l.film_id " +
                "where upper(f.name) like concat('%', upper(?),  '%') " +
                "group by f.film_id " +
                "order by f.film_id desc ";

        String sqlFilmAndDirector = "select f.film_id, " +
                "f.name, " +
                "f.mpa_id, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_name, " +
                "count(l.user_id) as flikes " +
                "from films as f " +
                "left join film_directors as fd on f.film_id = fd.film_id " +
                "left join directors as d on d.director_id = fd.director_id " +
                "left join mpa as m on f.mpa_id = m.mpa_id " +
                "left join likes As l on f.film_id = l.film_id " +
                "where upper(f.name) like concat('%', upper(?),  '%') OR " +
                "upper(d.name) like concat('%', upper(?),  '%') " +
                "group by f.film_id " +
                "order by f.film_id desc ";


        if (Arrays.stream(parameters).allMatch("director"::equals)) {
            return jdbcTemplate.query(sqlDirectorName, new FilmRowMapper(), query);
        } else if (Arrays.stream(parameters).allMatch("title"::equals)) {
            return jdbcTemplate.query(sqlFilmName, new FilmRowMapper(), query);
        } else {
            return jdbcTemplate.query(sqlFilmAndDirector, new FilmRowMapper(), query, query);
        }
    }

    private class FilmRowMapper implements RowMapper<Film> {
        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Film.builder()
                    .id(rs.getLong("film_id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(mpaRepository.getMpa(rs.getInt("mpa_id")))
                    .genres(genreRepository.loadFilmGenre(rs.getLong("film_id")))
                    .build();
        }
    }
}

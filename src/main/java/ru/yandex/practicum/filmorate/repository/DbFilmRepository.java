package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
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
import java.util.*;

@Repository
@RequiredArgsConstructor
public class DbFilmRepository implements FilmRepository {

    private final JdbcOperations jdbcTemplate;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;

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
        directorRepository.insertDirectors(film);
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
        directorRepository.removeFilmDirectors(film);
        if (film.getGenres().size() > 0) {
            genreRepository.insertGenres(film);
        }
        if (film.getDirectors().size() > 0) {
            directorRepository.insertDirectors(film);
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
    public void deleteFilm(long filmId) {
        final String sqlQuery = "DELETE FROM films " +
                "WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public void addLike(Film film, User user) {
        final String sqlQuery = "MERGE INTO likes AS l " +
                "USING (SELECT CAST(? AS INTEGER), CAST(? AS INTEGER)) AS s (film_id, user_id) " +
                "ON (l.film_id = s.film_id) AND (l.user_id = s.user_id) " +
                "WHEN NOT MATCHED THEN " +
                "INSERT (film_id, user_id) " +
                "VALUES (s.film_id, s.user_id);";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public List<Film> getPopularFilms(Long count, Integer genreId, Integer year) {
        if (Objects.equals(genreId, year)) {
            final String sqlQuery = "SELECT f.* " +
                    "FROM films AS f " +
                    "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(l.user_id) DESC " +
                    "LIMIT ?;";
            return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), count);
        } else {
            if (genreId == null) {
                final String sqlQuery = "SELECT f.* " +
                        "FROM films AS f " +
                        "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                        "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(l.user_id) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), year, count);
            } else if (year == null) {
                final String sqlQuery = "SELECT f.* " +
                        "FROM films AS f " +
                        "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                        "WHERE fg.genre_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(l.user_id) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), genreId, count);
            } else {
                final String sqlQuery = "SELECT f.* " +
                        "FROM films AS f " +
                        "LEFT JOIN film_genre AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                        "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = ? " +
                        "AND fg.genre_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(l.user_id) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), year, genreId, count);
            }
        }
    }

    @Override
    public List<Film> getAllDirectorFilms(Long directorId, String sortBy) {
        directorRepository.getDirector(directorId)
                .orElseThrow(() -> new NotFoundException("Director not found with id = " + directorId));
        final String sqlQuery;
        if (sortBy.equals("likes")) {
            sqlQuery = "SELECT f.* " +
                    "FROM films AS f " +
                    "RIGHT JOIN film_directors AS fd ON f.film_id = fd.film_id " +
                    "LEFT JOIN likes AS l ON l.film_id = f.film_id " +
                    "WHERE fd.director_id = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY COUNT(l.user_id);";
        } else {
            sqlQuery = "SELECT f.* " +
                    "FROM films AS f " +
                    "RIGHT JOIN film_directors AS fd ON f.film_id = fd.film_id " +
                    "WHERE fd.director_id = ? " +
                    "GROUP BY f.film_id " +
                    "ORDER BY EXTRACT(YEAR FROM CAST(f.release_date AS DATE));";
        }

        return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), directorId);
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sqlQuery = "SELECT f.* " +
                "FROM films AS f, likes AS l1, likes AS l2 " +
                "WHERE f.film_id = l1.film_id " +
                "AND f.film_id = l2.film_id " +
                "AND l1.user_id = ? " +
                "AND l2.user_id = ? " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(*) DESC;";

        return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), userId, friendId);

    }

    @Override
    public List<Film> searchFilms(String query, String fields) {
        StringBuilder sql = new StringBuilder("SELECT f.* "
                + "FROM films f "
                + "LEFT JOIN likes l ON f.film_id = l.film_id "
                + "LEFT JOIN mpa m ON m.mpa_id = f.mpa_id "
                + "LEFT JOIN film_directors fd ON f.film_id = fd.film_id "
                + "LEFT JOIN directors d ON fd.director_id = d.director_id ");
        if (fields.equals("title")) {
            sql.append("WHERE LOWER(f.name) LIKE LOWER('%").append(query).append("%') ");
        }
        if (fields.equals("director")) {
            sql.append("WHERE LOWER(d.director_name) LIKE LOWER('%").append(query).append("%') ");
        }
        if (fields.equals("title,director") || fields.equals("director,title")) {
            sql.append("WHERE LOWER(f.name) LIKE LOWER('%").append(query).append("%') ");
            sql.append("OR LOWER(d.director_name) LIKE LOWER('%").append(query).append("%') ");
        }
        sql.append("GROUP BY f.film_id, l.film_id " + "ORDER BY COUNT(l.film_id) DESC");
        return jdbcTemplate.query(sql.toString(), new FilmRowMapper());
    }

    @Override
    public List<Film> getFilmsRecommendation(long userId) {
        final List<Long> userLikes = getUserLikes(userId);
        if (userLikes.isEmpty()) {
            return Collections.emptyList();
        }
        int maxRecommendations = 10;
        String similarUsersQuery = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id IN (" + String.join(",", Collections.nCopies(userLikes.size(), "?")) + ") " +
                "AND user_id <> " + userId +
                "GROUP BY user_id " +
                "ORDER BY COUNT(*) DESC " +
                "LIMIT " + maxRecommendations + ";";
        List<Object> params = new ArrayList<>(userLikes);
        List<Long> similarUserIds = jdbcTemplate.queryForList(similarUsersQuery, Long.class, params.toArray());
        if (similarUserIds.isEmpty()) {
            return Collections.emptyList();
        }

        String recommendedFilmsQuery = "SELECT f.* " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE l.user_id = ? " +
                "AND NOT EXISTS " +
                "(SELECT 1 FROM likes lu WHERE lu.user_id = ? AND lu.film_id = f.film_id);";

        List<Long> allSimilarUserIds = new ArrayList<>(similarUserIds);
        return jdbcTemplate.query(recommendedFilmsQuery, new FilmRowMapper(), allSimilarUserIds.toArray(),userId);
    }

    private List<Long> getUserLikes(Long userId) {
        final String sqlQuery = "SELECT l.film_id " +
                "FROM likes AS l " +
                "WHERE l.user_id = ?;";
        return jdbcTemplate.queryForList(sqlQuery, Long.class, userId);
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
                    .directors(directorRepository.loadFilmDirector(rs.getLong("film_id")))
                    .build();
        }
    }
}

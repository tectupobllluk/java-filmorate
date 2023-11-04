package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.EventOperationEnum;
import ru.yandex.practicum.filmorate.model.EventTypeEnum;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DbFilmRepository implements FilmRepository {

    private final JdbcOperations jdbcTemplate;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final FeedRepository feedRepository;

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
        final String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        feedRepository.saveEvent(user.getId(), feedRepository.getEventTypeId(EventTypeEnum.LIKE), feedRepository.getOperationTypeId(EventOperationEnum.ADD), film.getId());

    }

    @Override
    public void deleteLike(Film film, User user) {
        final String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?;";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
        feedRepository.saveEvent(user.getId(), feedRepository.getEventTypeId(EventTypeEnum.LIKE), feedRepository.getOperationTypeId(EventOperationEnum.REMOVE), film.getId());
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
            if (genreId == -1) {
                final String sqlQuery = "SELECT f.* " +
                        "FROM films AS f " +
                        "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                        "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE)) = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(l.user_id) DESC " +
                        "LIMIT ?;";
                return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), year, count);
            } else if (year == -1) {
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
        if (query.isEmpty()) {
            return getAllFilms();
        }

        String sqlFilmName = "SELECT f.film_id, " +
                "f.name, " +
                "f.mpa_id, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_name, " +
                "COUNT(l.user_id) AS film_likes " +
                "FROM films AS f " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE UPPER(f.name) LIKE CONCAT('%', UPPER(?), '%') " +
                "GROUP BY f.film_id " +
                "ORDER BY f.film_id DESC; ";

        String sqlDirectorName = "SELECT f.film_id, " +
                "f.name, " +
                "f.mpa_id, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_name, " +
                "COUNT(l.user_id) AS film_likes " +
                "FROM films AS f " +
                "LEFT JOIN film_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE UPPER(d.director_name) LIKE CONCAT('%', UPPER(?), '%') " +
                "GROUP BY f.film_id " +
                "ORDER BY f.film_id DESC; ";

        String sqlFilmAndDirector = "SELECT f.film_id, " +
                "f.name, " +
                "f.mpa_id, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "m.mpa_name, " +
                "COUNT(l.user_id) AS film_likes " +
                "FROM films AS f " +
                "LEFT JOIN film_directors AS fd ON f.film_id = fd.film_id " +
                "LEFT JOIN directors AS d ON d.director_id = fd.director_id " +
                "LEFT JOIN mpa AS m ON f.mpa_id = m.mpa_id " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE UPPER(f.name) LIKE CONCAT('%', UPPER(?), '%') OR " +
                "UPPER(d.director_name) LIKE CONCAT('%', UPPER(?),  '%') " +
                "GROUP BY f.film_id " +
                "ORDER BY f.film_id DESC; ";

        if (fields.contains("title") && fields.contains("director")) {
            return jdbcTemplate.query(sqlFilmAndDirector, new FilmRowMapper(), query, query);
        } else if (fields.contains("title")) {
            return jdbcTemplate.query(sqlFilmName, new FilmRowMapper(), query);
        } else if (fields.contains("director")) {
            return jdbcTemplate.query(sqlDirectorName, new FilmRowMapper(), query);
        } else {
            throw new IllegalArgumentException("Неизвестное поле для поиска " + fields + ". Варианты: [director, title]");
        }
    }

    @Override
    public List<Film> getFilmsRecommendation(Long userId) {
        final List<Long> ids = getUserLikes(userId);
        if (ids.size() == 0) {
            return Collections.emptyList();
        }

        String sqlQuery = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id IN " +
                "(" +
                String.join(",", Collections.nCopies(ids.size(), "?")) +
                ") " +
                "GROUP BY user_id " +
                "ORDER BY COUNT(*) DESC;";

        List<Long> sqlUserId = jdbcTemplate.queryForList(sqlQuery, Long.class, ids.toArray());
        if (sqlUserId.size() == 1) {
            return Collections.emptyList();
        }

        sqlQuery = "SELECT f.* " +
                "FROM films AS f " +
                "LEFT JOIN likes AS l ON f.film_id = l.film_id " +
                "WHERE l.user_id = ? " +
                "AND NOT EXISTS " +
                "(SELECT 1 FROM likes lu WHERE lu.user_id = ? AND lu.film_id = f.film_id);";

        return jdbcTemplate.query(sqlQuery, new FilmRowMapper(), sqlUserId.get(1), userId);
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

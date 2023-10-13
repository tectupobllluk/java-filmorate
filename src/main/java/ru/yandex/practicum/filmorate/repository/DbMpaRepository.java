package ru.yandex.practicum.filmorate.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DbMpaRepository implements MpaRepository {
    private final JdbcOperations jdbcTemplate;

    @Override
    public Mpa getMpa(int mpaId) {
        final String sqlQuery = "SELECT * " +
                "FROM mpa " +
                "WHERE mpa_id = ?;";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, new MpaRowMapper(), mpaId);
        if (mpa.size() == 0) {
            throw new NotFoundException("Unknown MPA with " + mpaId + " id!");
        }
        return mpa.get(0);
    }

    @Override
    public List<Mpa> getAllMpa() {
        final String sqlQuery = "SELECT * " +
                "FROM mpa;";
        return jdbcTemplate.query(sqlQuery, new MpaRowMapper());
    }

    private static class MpaRowMapper implements RowMapper<Mpa> {
        @Override
        public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Mpa.builder()
                    .id(rs.getInt("mpa_id"))
                    .name(rs.getString("mpa_name"))
                    .build();
        }
    }
}

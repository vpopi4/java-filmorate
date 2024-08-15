package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.AbstractJdbcDao;

@Repository
public class JdbcFilmDao extends AbstractJdbcDao<Film> {
    @Autowired
    public JdbcFilmDao(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper) {
        super(jdbcTemplate, "films", rowMapper);
    }

    @Override
    protected String buildInsertQuery(Film entity) {
        return """
                INSERT INTO films (
                    id,
                    name,
                    description,
                    release_date,
                    duration_in_minutes,
                    mpa_rating_id
                ) VALUES (?, ?, ?, ?, ?, ?);
                """;
    }

    @Override
    protected String buildUpdateQuery(Film entity) {
        return """
                UPDATE
                    films
                SET
                    name = ?,
                    description = ?,
                    release_date = ?,
                    duration_in_minutes = ?,
                    mpa_rating_id = ?
                WHERE
                    id = ?;
                """;
    }

    @Override
    protected Object[] getInsertParams(Film film) {
        return new Object[] {
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpaRatingId()
        };
    }

    @Override
    protected Object[] getUpdateParams(Film film) {
        return new Object[]{
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpaRatingId(),
                film.getId()
        };
    }
}

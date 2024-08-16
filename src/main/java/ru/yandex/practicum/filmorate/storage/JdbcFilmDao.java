package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.AbstractJdbcDao;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcFilmDao extends AbstractJdbcDao<Film> {
    private final RowMapper<Genre> genreRowMapper;

    @Autowired
    public JdbcFilmDao(JdbcTemplate jdbcTemplate, RowMapper<Film> rowMapper, RowMapper<Genre> genreRowMapper) {
        super(jdbcTemplate, "films", rowMapper);

        this.genreRowMapper = genreRowMapper;
    }

    @Override
    public List<Film> getAll() throws DataAccessException {
        String sql = """
                SELECT
                    films.*,
                    mr.id AS rating_id,
                    mr.name AS rating_name,
                    mr.description AS rating_description
                FROM films
                LEFT OUTER JOIN mpa_ratings AS mr
                ON mr.id = mpa_rating_id;
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Film> getById(Integer id) throws DataAccessException {
        String sql = """
                SELECT
                    films.*,
                    mr.id AS rating_id,
                    mr.name AS rating_name,
                    mr.description AS rating_description
                FROM films
                LEFT OUTER JOIN mpa_ratings AS mr
                ON mr.id = mpa_rating_id
                WHERE films.id = ?;
                """;
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Set<Integer> getLikesUserId(Integer filmId) throws DataAccessException {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";

        List<Integer> likes = jdbcTemplate.queryForList(sql, Integer.class, filmId);

        return new HashSet<>(likes);
    }

    public List<Genre> getGenres(Integer filmId) throws DataAccessException {
        String sql = """
                SELECT g.id, g.name
                FROM genres AS g
                INNER JOIN film_genres AS fg
                ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;
        return jdbcTemplate.query(sql, genreRowMapper, filmId);
    }

    public void setGenres(Integer filmId, List<Genre> genres) {
        String deleteGenresSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, filmId);

        String insertGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertGenreSql, genres, genres.size(),
                (ps, genre) -> {
                    ps.setInt(1, filmId);
                    ps.setInt(2, genre.getId());
                });
    }

    public void putLike(Integer filmId, Integer userId) {
        String sql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        String sql = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
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
        return new Object[]{
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa() == null
                        ? null
                        : film.getMpa().getId()
        };
    }

    @Override
    protected Object[] getUpdateParams(Film film) {
        return new Object[]{
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa() == null
                        ? null
                        : film.getMpa().getId(),
                film.getId()
        };
    }
}

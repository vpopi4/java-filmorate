package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDao;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class JdbcFilmDao implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreMapper = ((rs, rowNum) -> Genre.builder()
            .id(rs.getInt("id"))
            .name(rs.getString("name"))
            .build());
    private final RowMapper<Film> mapper = ((rs, rowNum) -> {
        int id = rs.getInt("id");
        long durationInMinutes = rs.getLong("duration_in_minutes");
        MpaRating mpaRating = MpaRating.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating_name"))
                .description(rs.getString("rating_description"))
                .build();

        return Film.builder()
                .id(id)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(Duration.ofMinutes(durationInMinutes))
                .rating(mpaRating)
                .likesUserId(getLikesUserId(id))
                .genres(getGenres(id))
                .build();
    });

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
        return jdbcTemplate.query(sql, mapper);
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
        return jdbcTemplate.query(sql, mapper)
                .stream().findFirst();
    }

    @Override
    public Film create(Film film) throws AlreadyExistException, DataAccessException {
        String sql = """
                INSERT INTO films (
                    id,
                    name,
                    description,
                    release_date,
                    duration_in_minutes,
                    mpa_rating_id
                ) VALUES (?, ?, ?, ?, ?, ?);
                """;

        MpaRating rating = film.getRating();
        jdbcTemplate.update(sql,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                rating == null ? null : rating.getId()
        );

        return getById(film.getId())
                .orElseThrow(() -> new IllegalStateException("entity not found but created"));
    }

    @Override
    public Film update(Film film) throws NotFoundException, DataAccessException {
        String sql = """
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

        int rowsAffected = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getRating().getId(),
                film.getId()
        );

        if (rowsAffected != 1) {
            throw new NotFoundException();
        }

        String deleteGenresSql = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteGenresSql, film.getId());

        String insertGenreSql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertGenreSql, film.getGenres(), film.getGenres().size(),
                (ps, genre) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genre.getId());
                });

        String deleteLikesSql = "DELETE FROM film_likes WHERE film_id = ?";
        jdbcTemplate.update(deleteLikesSql, film.getId());

        String insertLikeSql = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(insertLikeSql, film.getLikesUserId(), film.getLikesUserId().size(),
                (ps, userId) -> {
                    ps.setInt(1, film.getId());
                    ps.setInt(2, userId);
                });

        return film;
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", id);
    }

    @Override
    public Integer getMaxId() throws DataAccessException {
        Integer result = jdbcTemplate.queryForObject("SELECT MAX(id) FROM films;", Integer.class);
        return result == null ? 0 : result;
    }

    private Set<Integer> getLikesUserId(Integer filmId) throws DataAccessException {
        String sql = "SELECT user_id FROM film_likes WHERE film_id = ?";

        List<Integer> likes = jdbcTemplate.queryForList(sql, Integer.class, filmId);

        return new HashSet<>(likes);
    }

    private List<Genre> getGenres(Integer filmId) throws DataAccessException {
        String sql = """
                SELECT g.id, g.name
                FROM genres AS g
                INNER JOIN film_genres AS fg
                ON g.id = fg.genre_id
                WHERE fg.film_id = ?
                """;
        return jdbcTemplate.query(sql, genreMapper, filmId);
    }
}

package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.AbstractJdbcDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class JdbcGenreDao extends AbstractJdbcDao<Genre> {
    @Autowired
    public JdbcGenreDao(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, "genres", rowMapper);
    }

    public List<Genre> getManyById(List<Integer> ids) {
        String placeholders = ids.stream()
                .map(id -> "?")
                .collect(Collectors.joining(","));

        String sql = "SELECT * FROM genres WHERE id IN (" + placeholders + ")";

        Object[] params = ids.toArray();

        return jdbcTemplate.query(sql, rowMapper, params);
    }

    @Override
    protected String buildInsertQuery(Genre entity) {
        return "INSERT INTO genres (id, name) VALUES (?, ?)";
    }

    @Override
    protected String buildUpdateQuery(Genre entity) {
        return "UPDATE genres SET name = ? WHERE id = ?";
    }

    @Override
    protected Object[] getInsertParams(Genre genre) {
        return new Object[]{
                genre.getId(),
                genre.getName()
        };
    }

    @Override
    protected Object[] getUpdateParams(Genre genre) {
        return new Object[]{
                genre.getName(),
                genre.getId()
        };
    }
}

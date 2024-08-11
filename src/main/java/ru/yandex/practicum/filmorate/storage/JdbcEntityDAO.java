package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class JdbcEntityDAO<E> implements EntityDAO<E> {
    protected final JdbcTemplate jdbcTemplate;
    protected abstract RowMapper<E> getRowMapper();
    protected abstract String getTableName();

    public JdbcEntityDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<E> getById(Integer id) {
        try {
            String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
            return jdbcTemplate.query(sql, getRowMapper(), id).stream().findFirst();
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<E> getAll() {
        try {
            String sql = "SELECT * FROM " + getTableName();
            return jdbcTemplate.query(sql, getRowMapper());
        } catch (DataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

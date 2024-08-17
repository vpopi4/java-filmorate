package ru.yandex.practicum.filmorate.storage.interfaces;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJdbcDao<E> implements Dao<E> {
    protected final JdbcTemplate jdbcTemplate;
    protected final String tableName;
    protected final RowMapper<E> rowMapper;

    public AbstractJdbcDao(JdbcTemplate jdbcTemplate, String tableName, RowMapper<E> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tableName = tableName;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<E> getById(Integer id) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        try {
            return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<E> getAll() {
        String sql = "SELECT * FROM " + tableName;

        try {
            return jdbcTemplate.query(sql, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public void create(E entity) {
        String sql = buildInsertQuery(entity);
        jdbcTemplate.update(sql, getInsertParams(entity));
    }

    @Override
    public void update(E entity) {
        String sql = buildUpdateQuery(entity);
        jdbcTemplate.update(sql, getUpdateParams(entity));
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Integer getMaxId() throws DataAccessException {
        String sql = "SELECT MAX(id) FROM " + tableName;
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return result == null ? 0 : result;
    }

    protected abstract String buildInsertQuery(E entity);

    protected abstract String buildUpdateQuery(E entity);

    protected abstract Object[] getInsertParams(E entity);

    protected abstract Object[] getUpdateParams(E entity);
}

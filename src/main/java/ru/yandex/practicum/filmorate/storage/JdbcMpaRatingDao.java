package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.AbstractJdbcDao;

@Repository
public class JdbcMpaRatingDao extends AbstractJdbcDao<MpaRating> {
    @Autowired
    public JdbcMpaRatingDao(JdbcTemplate jdbcTemplate, RowMapper<MpaRating> rowMapper) {
        super(jdbcTemplate, "mpa_ratings", rowMapper);
    }

    @Override
    protected String buildInsertQuery(MpaRating entity) {
        return "INSERT INTO mpa_ratings (id, name, description) VALUES (?, ? , ?)";
    }

    @Override
    protected String buildUpdateQuery(MpaRating entity) {
        return "UPDATE mpa_ratings SET name = ?, description = ? WHERE id = ?";
    }

    @Override
    protected Object[] getInsertParams(MpaRating entity) {
        return new Object[]{
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        };
    }

    @Override
    protected Object[] getUpdateParams(MpaRating entity) {
        return new Object[]{
                entity.getName(),
                entity.getDescription(),
                entity.getId()
        };
    }
}

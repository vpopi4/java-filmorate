package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

@Repository
public class JdbcGenreDao extends JdbcEntityDao<Genre> {
    @Autowired
    public JdbcGenreDao(JdbcTemplate jdbcTemplate, RowMapper<Genre> rowMapper) {
        super(jdbcTemplate, "genres", rowMapper);
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

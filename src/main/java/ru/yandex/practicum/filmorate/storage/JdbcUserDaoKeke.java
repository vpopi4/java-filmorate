package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.AbstractJdbcDao;
import ru.yandex.practicum.filmorate.storage.interfaces.UserDao;

import java.util.Optional;

@Repository
public class JdbcUserDaoKeke extends AbstractJdbcDao<User> implements UserDao {
    @Autowired
    public JdbcUserDaoKeke(JdbcTemplate jdbcTemplate, RowMapper<User> rowMapper) {
        super(jdbcTemplate, "users", rowMapper);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try {
            return jdbcTemplate.query(sql, rowMapper, email).stream().findFirst();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getByLogin(String login) {
        String sql = "SELECT * FROM users WHERE login = ?";

        try {
            return jdbcTemplate.query(sql, rowMapper, login).stream().findFirst();
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    protected String buildInsertQuery(User entity) {
        return "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)";
    }

    @Override
    protected String buildUpdateQuery(User entity) {
        return "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";
    }

    @Override
    protected Object[] getInsertParams(User user) {
        return new Object[]{
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        };
    }

    @Override
    protected Object[] getUpdateParams(User user) {
        return new Object[]{
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        };
    }
}

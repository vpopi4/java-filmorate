package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserRowMapper;

@Repository
public class JdbcUserDAO extends JdbcEntityDAO<User> implements UserDAO {
    @Autowired
    public JdbcUserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<User> getRowMapper() {
        return new UserRowMapper();
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    public User create(User user) throws AlreadyExistException, DataAccessException {
        try {
            String sql = "INSERT INTO users (id, email, login, name, birthday) " +
                    "VALUES (?, ?, ?, ?, ?);";

            jdbcTemplate.update(sql,
                    user.getId(),
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday()
            );

            return getById(user.getId())
                    .orElseThrow(() -> new IllegalStateException("entity not found but created"));
        } catch (DataAccessException e) {
            throw new AlreadyExistException(e.getMessage(), e);
        }
    }

    @Override
    public User update(User user) throws NotFoundException {
        String sql = """
                UPDATE
                    users
                SET
                    email = ?,
                    login = ?,
                    name = ?,
                    birthday = ?
                WHERE
                    id = ?;
                """;

        int rowsAffected = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );

        if (rowsAffected != 1) {
            throw new NotFoundException();
        }

        return user;
    }

    @Override
    public boolean checkUnique(String email, String login) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? OR login = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, login);

        return count != null && count == 0;
    }
}

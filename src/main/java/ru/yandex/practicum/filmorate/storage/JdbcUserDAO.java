package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JdbcUserDao implements UserDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    @Override
    public List<User> getAll() throws DataAccessException {
        return jdbcTemplate.query("SELECT * FROM users;", userRowMapper);
    }

    @Override
    public User getById(Integer id) throws NotFoundException, DataAccessException {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?", userRowMapper, id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User[id=" + id + "] not found"));
    }

    @Override
    public User getByEmail(String email) throws NotFoundException, DataAccessException {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?", userRowMapper, email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User[email=" + email + "] not found"));
    }

    @Override
    public User getByLogin(String login) throws NotFoundException, DataAccessException {
        return jdbcTemplate.query("SELECT * FROM users WHERE login = ?", userRowMapper, login)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException("User[login=" + login + "] not found"));
    }

    @Override
    public User create(User user) throws AlreadyExistException, DataAccessException {
        checkUnique(user.getEmail(), user.getLogin());

        String sql = "INSERT INTO users (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?);";

        int affectedRows = jdbcTemplate.update(sql,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        if (affectedRows != 1) {
            throw new IllegalStateException("User not created: " + user);
        }

        try {
            return getById(user.getId());
        } catch (NotFoundException e) {
            throw new IllegalStateException("User not found but created: " + user);
        }
    }

    @Override
    public User update(User user) throws NotFoundException, AlreadyExistException, DataAccessException {
        checkUnique(user.getEmail(), user.getLogin());

        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?;";

        int rowsAffected = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (rowsAffected != 1) {
            throw new NotFoundException("User not found");
        }

        return user;
    }

    private void checkUnique(String email, String login) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? OR login = ?";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, login);

        if (count == null || count != 0) {
            throw new AlreadyExistException("User[email=" + email + " | login=" + login + "] already exist");
        }
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}

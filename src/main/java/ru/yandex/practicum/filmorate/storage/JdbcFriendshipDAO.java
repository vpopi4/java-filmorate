package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.exception.AlreadyExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.Timestamp;
import java.util.Optional;

public class JdbcFriendshipDAO extends JdbcEntityDAO<Friendship> {
    public JdbcFriendshipDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    protected RowMapper<Friendship> getRowMapper() {
        return ((rs, rowNum) -> {
            Timestamp createdAt = rs.getTimestamp("created_at");

            return Friendship.builder()
                    .userIdFrom(rs.getInt("user_id_from"))
                    .userIdTo(rs.getInt("user_id_to"))
                    .isAccepted(rs.getBoolean("is_accepted"))
                    .createdAt(createdAt != null
                            ? createdAt.toInstant()
                            : null)
                    .build();
        });
    }

    @Override
    protected String getTableName() {
        return "friendships";
    }

    @Override
    public Optional<Friendship> getById(Integer id) {
        throw new UnsupportedOperationException("table friendships does not contain id field");
    }

    @Override
    public void delete(Integer id) throws DataAccessException {
        throw new UnsupportedOperationException("table friendships does not contain id field");
    }

    @Override
    public Friendship create(Friendship friendship) throws AlreadyExistException, DataAccessException {
        try {
            String sql = "INSERT INTO friendship (user_id_from, user_id_to, is_accepted) " +
                    "VALUES (?, ?, ?) RETURNING *;";

            return jdbcTemplate.queryForObject(sql, getRowMapper(),
                    friendship.getUserIdFrom(),
                    friendship.getUserIdTo(),
                    friendship.getIsAccepted()
            );
        } catch (DataAccessException e) {
            throw new AlreadyExistException(e.getMessage(), e);
        }
    }

    @Override
    public Friendship update(Friendship friendship) throws NotFoundException, DataAccessException {
        String sql = """
                UPDATE
                    friendship
                SET
                    is_accepted = ?
                WHERE
                    user_id_from = ?
                AND
                    user_id_to = ?;
                """;

        int rowsAffected = jdbcTemplate.update(sql,
                friendship.getIsAccepted(),
                friendship.getUserIdFrom(),
                friendship.getUserIdTo()
        );

        if (rowsAffected != 1) {
            throw new NotFoundException();
        }

        return friendship;
    }

}

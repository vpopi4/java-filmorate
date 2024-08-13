package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Date birthday = rs.getDate("birthday");
        Timestamp createdAt = rs.getTimestamp("created_at");

        return User.builder()
                .id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(birthday != null ? birthday.toLocalDate() : null)
                .createdAt(createdAt != null ? createdAt.toInstant() : null)
                .friendsId(null)
                .build();
    }
}

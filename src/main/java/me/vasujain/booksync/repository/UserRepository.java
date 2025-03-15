package me.vasujain.booksync.repository;

import me.vasujain.booksync.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id((UUID) rs.getObject("id"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .passwordHash(rs.getString("password_hash"))
                    .role(rs.getString("role"))
                    .firstName(rs.getString("first_name"))
                    .lastName(rs.getString("last_name"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .build();
        }
    };

    public List<User> findAll(int offset, int limit, String sortBy, String sortDir) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "username";
        }
        if (sortDir == null || (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "ASC";
        }
        String sql = "SELECT * FROM \"user\" ORDER BY " + sortBy + " " + sortDir + " LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);
        params.addValue("offset", offset);
        return jdbcTemplate.query(sql, params, userRowMapper);
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM \"user\"";
        return jdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public Optional<User> findById(UUID id) {
        String sql = "SELECT * FROM \"user\" WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<User> users = jdbcTemplate.query(sql, params, userRowMapper);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public int save(User user) {
        String sql = "INSERT INTO \"user\" (id, username, email, password_hash, role, first_name, last_name, created_at, updated_at) " +
                "VALUES (:id, :username, :email, :password_hash, :role, :first_name, :last_name, :created_at, :updated_at)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", user.getId());
        params.addValue("username", user.getUsername());
        params.addValue("email", user.getEmail());
        params.addValue("password_hash", user.getPasswordHash());
        params.addValue("role", user.getRole());
        params.addValue("first_name", user.getFirstName());
        params.addValue("last_name", user.getLastName());
        params.addValue("created_at", user.getCreatedAt());
        params.addValue("updated_at", user.getUpdatedAt());
        return jdbcTemplate.update(sql, params);
    }

    public int update(User user) {
        String sql = "UPDATE \"user\" SET username = :username, email = :email, role = :role, first_name = :first_name, " +
                "last_name = :last_name, updated_at = :updated_at WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("username", user.getUsername());
        params.addValue("email", user.getEmail());
        params.addValue("role", user.getRole());
        params.addValue("first_name", user.getFirstName());
        params.addValue("last_name", user.getLastName());
        params.addValue("updated_at", user.getUpdatedAt());
        params.addValue("id", user.getId());
        return jdbcTemplate.update(sql, params);
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM \"user\" WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update(sql, params);
    }
}

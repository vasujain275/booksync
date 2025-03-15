package me.vasujain.booksync.repository;

import me.vasujain.booksync.model.Loan;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class LoanRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public LoanRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Loan> loanRowMapper = new RowMapper<Loan>() {
        @Override
        public Loan mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Loan.builder()
                    .id((UUID) rs.getObject("id"))
                    .userId((UUID) rs.getObject("user_id"))
                    .bookId((UUID) rs.getObject("book_id"))
                    .borrowedDate(rs.getDate("borrowed_date").toLocalDate())
                    .dueDate(rs.getDate("due_date").toLocalDate())
                    .returnedDate(rs.getDate("returned_date") != null ? rs.getDate("returned_date").toLocalDate() : null)
                    .status(rs.getString("status"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .build();
        }
    };

    public List<Loan> findAll(int offset, int limit, String sortBy, String sortDir) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "borrowed_date";
        }
        if (sortDir == null || (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "ASC";
        }
        String sql = "SELECT * FROM loan ORDER BY " + sortBy + " " + sortDir + " LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);
        params.addValue("offset", offset);
        return jdbcTemplate.query(sql, params, loanRowMapper);
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM loan";
        return jdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public Optional<Loan> findById(UUID id) {
        String sql = "SELECT * FROM loan WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Loan> loans = jdbcTemplate.query(sql, params, loanRowMapper);
        return loans.isEmpty() ? Optional.empty() : Optional.of(loans.get(0));
    }

    public int save(Loan loan) {
        String sql = "INSERT INTO loan (id, user_id, book_id, borrowed_date, due_date, returned_date, status, created_at, updated_at) " +
                "VALUES (:id, :user_id, :book_id, :borrowed_date, :due_date, :returned_date, :status, :created_at, :updated_at)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", loan.getId());
        params.addValue("user_id", loan.getUserId());
        params.addValue("book_id", loan.getBookId());
        params.addValue("borrowed_date", loan.getBorrowedDate());
        params.addValue("due_date", loan.getDueDate());
        params.addValue("returned_date", loan.getReturnedDate());
        params.addValue("status", loan.getStatus());
        params.addValue("created_at", loan.getCreatedAt());
        params.addValue("updated_at", loan.getUpdatedAt());
        return jdbcTemplate.update(sql, params);
    }

    public int update(Loan loan) {
        String sql = "UPDATE loan SET returned_date = :returned_date, status = :status, updated_at = :updated_at " +
                "WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("returned_date", loan.getReturnedDate());
        params.addValue("status", loan.getStatus());
        params.addValue("updated_at", loan.getUpdatedAt());
        params.addValue("id", loan.getId());
        return jdbcTemplate.update(sql, params);
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM loan WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update(sql, params);
    }
}

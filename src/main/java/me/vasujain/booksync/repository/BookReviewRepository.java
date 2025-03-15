package me.vasujain.booksync.repository;

import me.vasujain.booksync.model.BookReview;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookReviewRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BookReviewRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BookReview> reviewRowMapper = new RowMapper<BookReview>() {
        @Override
        public BookReview mapRow(ResultSet rs, int rowNum) throws SQLException {
            return BookReview.builder()
                    .id((UUID) rs.getObject("id"))
                    .bookId((UUID) rs.getObject("book_id"))
                    .userId((UUID) rs.getObject("user_id"))
                    .rating(rs.getInt("rating"))
                    .reviewText(rs.getString("review_text"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .build();
        }
    };

    public List<BookReview> findAll(int offset, int limit, String sortBy, String sortDir) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "created_at";
        }
        if (sortDir == null || (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "ASC";
        }
        String sql = "SELECT * FROM book_review ORDER BY " + sortBy + " " + sortDir + " LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);
        params.addValue("offset", offset);
        return jdbcTemplate.query(sql, params, reviewRowMapper);
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM book_review";
        return jdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public Optional<BookReview> findById(UUID id) {
        String sql = "SELECT * FROM book_review WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<BookReview> reviews = jdbcTemplate.query(sql, params, reviewRowMapper);
        return reviews.isEmpty() ? Optional.empty() : Optional.of(reviews.get(0));
    }

    public int save(BookReview review) {
        String sql = "INSERT INTO book_review (id, book_id, user_id, rating, review_text, created_at) " +
                "VALUES (:id, :book_id, :user_id, :rating, :review_text, :created_at)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", review.getId());
        params.addValue("book_id", review.getBookId());
        params.addValue("user_id", review.getUserId());
        params.addValue("rating", review.getRating());
        params.addValue("review_text", review.getReviewText());
        params.addValue("created_at", review.getCreatedAt());
        return jdbcTemplate.update(sql, params);
    }

    public int update(BookReview review) {
        String sql = "UPDATE book_review SET rating = :rating, review_text = :review_text WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("rating", review.getRating());
        params.addValue("review_text", review.getReviewText());
        params.addValue("id", review.getId());
        return jdbcTemplate.update(sql, params);
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM book_review WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcTemplate.update(sql, params);
    }
}

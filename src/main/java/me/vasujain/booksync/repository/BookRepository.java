package me.vasujain.booksync.repository;

import me.vasujain.booksync.model.Book;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BookRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = new RowMapper<Book>() {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Book.builder()
                    .id((UUID) rs.getObject("id"))
                    .title(rs.getString("title"))
                    .authors(rs.getString("authors") != null ? Arrays.asList(rs.getString("authors").split(", ")) : Collections.emptyList())
                    .description(rs.getString("description"))
                    .publisher(rs.getString("publisher"))
                    .publishedDate(rs.getString("published_date"))
                    .category(rs.getString("category"))
                    .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                    .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
                    .build();
        }
    };

    public List<Book> findAll(int offset, int limit, String sortBy, String sortDir) {
        // Validate sorting: if missing or invalid, default to created_at DESC
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "created_at";
        }
        if (sortDir == null || (!sortDir.equalsIgnoreCase("ASC") && !sortDir.equalsIgnoreCase("DESC"))) {
            sortDir = "DESC";
        }
        String sql = "SELECT * FROM book ORDER BY " + sortBy + " " + sortDir + " LIMIT :limit OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit", limit);
        params.addValue("offset", offset);
        return namedParameterJdbcTemplate.query(sql, params, bookRowMapper);
    }

    public int countAll() {
        String sql = "SELECT COUNT(*) FROM book";
        return namedParameterJdbcTemplate.getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    public Optional<Book> findById(UUID id) {
        String sql = "SELECT * FROM book WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        List<Book> books = namedParameterJdbcTemplate.query(sql, params, bookRowMapper);
        return books.isEmpty() ? Optional.empty() : Optional.of(books.get(0));
    }

    public int save(Book book) {
        String sql = "INSERT INTO book (id, title, authors, description, publisher, published_date, category, created_at, updated_at) " +
                "VALUES (:id, :title, :authors, :description, :publisher, :published_date, :category, :created_at, :updated_at)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("title", book.getTitle());
        params.addValue("authors", String.join(", ", book.getAuthors()));
        params.addValue("description", book.getDescription());
        params.addValue("publisher", book.getPublisher());
        params.addValue("published_date", book.getPublishedDate());
        params.addValue("category", book.getCategory());
        params.addValue("created_at", book.getCreatedAt());
        params.addValue("updated_at", book.getUpdatedAt());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(Book book) {
        String sql = "UPDATE book SET title = :title, authors = :authors, description = :description, publisher = :publisher, " +
                "published_date = :published_date, category = :category, updated_at = :updated_at WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authors", String.join(", ", book.getAuthors()));
        params.addValue("description", book.getDescription());
        params.addValue("publisher", book.getPublisher());
        params.addValue("published_date", book.getPublishedDate());
        params.addValue("category", book.getCategory());
        params.addValue("updated_at", book.getUpdatedAt());
        params.addValue("id", book.getId());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM book WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return namedParameterJdbcTemplate.update(sql, params);
    }
}
package me.vasujain.booksync.repository;

import me.vasujain.booksync.model.Book;
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
                    .isbn(rs.getString("isbn"))
                    .title(rs.getString("title"))
                    .authors(rs.getString("authors"))
                    .publishDate(rs.getString("publish_date"))
                    .publisher(rs.getString("publisher"))
                    .coverId(rs.getObject("cover_id") != null ? rs.getInt("cover_id") : null)
                    .firstSentence(rs.getString("first_sentence"))
                    .numberOfPages(rs.getObject("number_of_pages") != null ? rs.getInt("number_of_pages") : null)
                    .isbn10(rs.getString("isbn_10"))
                    .isbn13(rs.getString("isbn_13"))
                    .ocaid(rs.getString("ocaid"))
                    .workKey(rs.getString("work_key"))
                    .category(rs.getString("category"))
                    .totalCopies(rs.getInt("total_copies"))
                    .availableCopies(rs.getInt("available_copies"))
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
        String sql = "INSERT INTO book (id, isbn, title, authors, publish_date, publisher, cover_id, first_sentence, " +
                "number_of_pages, isbn_10, isbn_13, ocaid, work_key, category, total_copies, available_copies, created_at, updated_at) " +
                "VALUES (:id, :isbn, :title, :authors, :publish_date, :publisher, :cover_id, :first_sentence, " +
                ":number_of_pages, :isbn_10, :isbn_13, :ocaid, :work_key, :category, :total_copies, :available_copies, :created_at, :updated_at)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", book.getId());
        params.addValue("isbn", book.getIsbn());
        params.addValue("title", book.getTitle());
        params.addValue("authors", book.getAuthors());
        params.addValue("publish_date", book.getPublishDate());
        params.addValue("publisher", book.getPublisher());
        params.addValue("cover_id", book.getCoverId());
        params.addValue("first_sentence", book.getFirstSentence());
        params.addValue("number_of_pages", book.getNumberOfPages());
        params.addValue("isbn_10", book.getIsbn10());
        params.addValue("isbn_13", book.getIsbn13());
        params.addValue("ocaid", book.getOcaid());
        params.addValue("work_key", book.getWorkKey());
        params.addValue("category", book.getCategory());
        params.addValue("total_copies", book.getTotalCopies());
        params.addValue("available_copies", book.getAvailableCopies());
        params.addValue("created_at", book.getCreatedAt());
        params.addValue("updated_at", book.getUpdatedAt());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    public int update(Book book) {
        String sql = "UPDATE book SET title = :title, authors = :authors, publish_date = :publish_date, " +
                "publisher = :publisher, cover_id = :cover_id, first_sentence = :first_sentence, " +
                "number_of_pages = :number_of_pages, isbn_10 = :isbn_10, isbn_13 = :isbn_13, " +
                "ocaid = :ocaid, work_key = :work_key, category = :category, total_copies = :total_copies, " +
                "available_copies = :available_copies, updated_at = :updated_at WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", book.getTitle());
        params.addValue("authors", book.getAuthors());
        params.addValue("publish_date", book.getPublishDate());
        params.addValue("publisher", book.getPublisher());
        params.addValue("cover_id", book.getCoverId());
        params.addValue("first_sentence", book.getFirstSentence());
        params.addValue("number_of_pages", book.getNumberOfPages());
        params.addValue("isbn_10", book.getIsbn10());
        params.addValue("isbn_13", book.getIsbn13());
        params.addValue("ocaid", book.getOcaid());
        params.addValue("work_key", book.getWorkKey());
        params.addValue("category", book.getCategory());
        params.addValue("total_copies", book.getTotalCopies());
        params.addValue("available_copies", book.getAvailableCopies());
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

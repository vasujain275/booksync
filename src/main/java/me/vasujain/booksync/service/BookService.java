package me.vasujain.booksync.service;

import me.vasujain.booksync.dto.BookCreateDTO;
import me.vasujain.booksync.dto.BookUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.expection.ResourceNotFoundException;
import me.vasujain.booksync.model.Book;
import me.vasujain.booksync.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Object getAllBooks(boolean paginate, int page, int size, String sortBy, String sortDir) {
        if (paginate) {
            int offset = page * size;
            List<Book> books = bookRepository.findAll(offset, size, sortBy, sortDir);
            int totalElements = bookRepository.countAll();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            return PaginatedResult.<Book>builder()
                    .content(books)
                    .currentPage(page)
                    .pageSize(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        } else {
            return bookRepository.findAll(0, Integer.MAX_VALUE, sortBy, sortDir);
        }
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Transactional
    public Book createBook(BookCreateDTO bookCreateDTO) {
        Book book = Book.builder()
                .id(UUID.randomUUID())
                .title(bookCreateDTO.getTitle())
                .authors(bookCreateDTO.getAuthors())
                .description(bookCreateDTO.getDescription())
                .publisher(bookCreateDTO.getPublisher())
                .publishedDate(bookCreateDTO.getPublishedDate())
                .category(bookCreateDTO.getCategory())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookRepository.save(book);
        return book;
    }

    @Transactional
    public Book updateBook(UUID id, BookUpdateDTO updateDTO) {
        Book existingBook = getBookById(id);
        existingBook.setTitle(updateDTO.getTitle());
        existingBook.setAuthors(updateDTO.getAuthors());
        existingBook.setDescription(updateDTO.getDescription());
        existingBook.setPublisher(updateDTO.getPublisher());
        existingBook.setPublishedDate(updateDTO.getPublishedDate());
        existingBook.setCategory(updateDTO.getCategory());
        existingBook.setUpdatedAt(LocalDateTime.now());
        bookRepository.update(existingBook);
        return existingBook;
    }

    @Transactional
    public void deleteBook(UUID id) {
        getBookById(id); // Verify existence; throws exception if not found.
        bookRepository.deleteById(id);
    }
}
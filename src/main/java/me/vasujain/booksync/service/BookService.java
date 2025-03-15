package me.vasujain.booksync.service;

import me.vasujain.booksync.dto.BookCreateDTO;
import me.vasujain.booksync.dto.BookUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.expection.ResourceNotFoundException;
import me.vasujain.booksync.model.Book;
import me.vasujain.booksync.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final RestTemplate restTemplate;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.restTemplate = new RestTemplate();
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
            // When not paginated, sort still applies.
            return bookRepository.findAll(0, Integer.MAX_VALUE, sortBy, sortDir);
        }
    }

    public Book getBookById(UUID id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        return optionalBook.get();
    }

    @Transactional
    public Book createBook(BookCreateDTO bookCreateDTO) {
        String isbn = bookCreateDTO.getIsbn();
        String url = "https://openlibrary.org/isbn/" + isbn + ".json";
        // Call the external OpenLibrary API and map the response to a Book.
        Book openLibraryBook = restTemplate.getForObject(url, Book.class);
        if (openLibraryBook == null) {
            throw new RuntimeException("Unable to fetch book details for ISBN: " + isbn);
        }
        // Manually generate UUID and timestamps.
        openLibraryBook.setId(UUID.randomUUID());
        openLibraryBook.setCreatedAt(LocalDateTime.now());
        openLibraryBook.setUpdatedAt(LocalDateTime.now());
        bookRepository.save(openLibraryBook);
        return openLibraryBook;
    }

    @Transactional
    public Book updateBook(UUID id, BookUpdateDTO updateDTO) {
        Book existingBook = getBookById(id);
        if (updateDTO.getCategory() != null) {
            existingBook.setCategory(updateDTO.getCategory());
        }
        if (updateDTO.getTotalCopies() != null) {
            existingBook.setTotalCopies(updateDTO.getTotalCopies());
        }
        if (updateDTO.getAvailableCopies() != null) {
            existingBook.setAvailableCopies(updateDTO.getAvailableCopies());
        }
        // Update the updatedAt timestamp.
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

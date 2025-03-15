package me.vasujain.booksync.controller;

import me.vasujain.booksync.dto.ApiResponse;
import me.vasujain.booksync.dto.BookCreateDTO;
import me.vasujain.booksync.dto.BookUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.model.Book;
import me.vasujain.booksync.service.BookService;
import me.vasujain.booksync.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getBooks(
            @RequestParam(required = false, defaultValue = "false") boolean paginate,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "created_at") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") String sortDir) {
        Object result = bookService.getAllBooks(paginate, page, size, sortBy, sortDir);
        ApiResponse<?> response;
        if (paginate) {
            response = ResponseUtil.successPaginated((PaginatedResult<?>) result, "Books retrieved successfully");
        } else {
            response = ResponseUtil.success(result, "Books retrieved successfully");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> getBookById(@PathVariable("id") UUID id) {
        Book book = bookService.getBookById(id);
        ApiResponse<Book> response = ResponseUtil.success(book, "Book retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@RequestBody BookCreateDTO bookCreateDTO) {
        Book createdBook = bookService.createBook(bookCreateDTO);
        ApiResponse<Book> response = ResponseUtil.success(createdBook, "Book created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@PathVariable("id") UUID id,
                                                        @RequestBody BookUpdateDTO updateDTO) {
        Book updatedBook = bookService.updateBook(id, updateDTO);
        ApiResponse<Book> response = ResponseUtil.success(updatedBook, "Book updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") UUID id) {
        bookService.deleteBook(id);
        ApiResponse<Void> response = ResponseUtil.success(null, "Book deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}

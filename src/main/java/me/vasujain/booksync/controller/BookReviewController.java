package me.vasujain.booksync.controller;

import me.vasujain.booksync.dto.ApiResponse;
import me.vasujain.booksync.dto.BookReviewCreateDTO;
import me.vasujain.booksync.dto.BookReviewUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.model.BookReview;
import me.vasujain.booksync.service.BookReviewService;
import me.vasujain.booksync.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookreviews")
public class BookReviewController {

    private final BookReviewService reviewService;

    public BookReviewController(BookReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getBookReviews(@RequestParam(required = false, defaultValue = "false") boolean paginate,
                                                         @RequestParam(required = false, defaultValue = "0") int page,
                                                         @RequestParam(required = false, defaultValue = "10") int size,
                                                         @RequestParam(required = false, defaultValue = "created_at") String sortBy,
                                                         @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        Object result = reviewService.getAllBookReviews(paginate, page, size, sortBy, sortDirection);
        ApiResponse<?> response;
        if (paginate) {
            response = ResponseUtil.successPaginated((PaginatedResult<?>) result, "BookReviews retrieved successfully");
        } else {
            response = ResponseUtil.success(result, "BookReviews retrieved successfully");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookReview>> getBookReviewById(@PathVariable("id") UUID id) {
        BookReview review = reviewService.getBookReviewById(id);
        ApiResponse<BookReview> response = ResponseUtil.success(review, "BookReview retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookReview>> createBookReview(@RequestBody BookReviewCreateDTO createDTO) {
        BookReview review = reviewService.createBookReview(createDTO);
        ApiResponse<BookReview> response = ResponseUtil.success(review, "BookReview created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookReview>> updateBookReview(@PathVariable("id") UUID id,
                                                                    @RequestBody BookReviewUpdateDTO updateDTO) {
        BookReview review = reviewService.updateBookReview(id, updateDTO);
        ApiResponse<BookReview> response = ResponseUtil.success(review, "BookReview updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBookReview(@PathVariable("id") UUID id) {
        reviewService.deleteBookReview(id);
        ApiResponse<Void> response = ResponseUtil.success(null, "BookReview deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}

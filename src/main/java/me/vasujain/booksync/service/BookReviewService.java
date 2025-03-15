package me.vasujain.booksync.service;

import me.vasujain.booksync.dto.BookReviewCreateDTO;
import me.vasujain.booksync.dto.BookReviewUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.expection.ResourceNotFoundException;
import me.vasujain.booksync.model.BookReview;
import me.vasujain.booksync.repository.BookReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookReviewService {

    private final BookReviewRepository reviewRepository;

    public BookReviewService(BookReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Object getAllBookReviews(boolean paginate, int page, int size, String sortBy, String sortDir) {
        if (paginate) {
            int offset = page * size;
            List<BookReview> reviews = reviewRepository.findAll(offset, size, sortBy, sortDir);
            int totalElements = reviewRepository.countAll();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            return PaginatedResult.<BookReview>builder()
                    .content(reviews)
                    .currentPage(page)
                    .pageSize(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        } else {
            return reviewRepository.findAll(0, Integer.MAX_VALUE, sortBy, sortDir);
        }
    }

    public BookReview getBookReviewById(UUID id) {
        Optional<BookReview> reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            throw new ResourceNotFoundException("BookReview not found with id: " + id);
        }
        return reviewOpt.get();
    }

    @Transactional
    public BookReview createBookReview(BookReviewCreateDTO createDTO) {
        BookReview review = BookReview.builder()
                .id(UUID.randomUUID())
                .bookId(createDTO.getBookId())
                .userId(createDTO.getUserId())
                .rating(createDTO.getRating())
                .reviewText(createDTO.getReviewText())
                .createdAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);
        return review;
    }

    @Transactional
    public BookReview updateBookReview(UUID id, BookReviewUpdateDTO updateDTO) {
        BookReview existingReview = getBookReviewById(id);
        if (updateDTO.getRating() != null) {
            existingReview.setRating(updateDTO.getRating());
        }
        if (updateDTO.getReviewText() != null) {
            existingReview.setReviewText(updateDTO.getReviewText());
        }
        reviewRepository.update(existingReview);
        return existingReview;
    }

    @Transactional
    public void deleteBookReview(UUID id) {
        getBookReviewById(id);
        reviewRepository.deleteById(id);
    }
}

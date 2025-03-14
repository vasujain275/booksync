package me.vasujain.booksync.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BookReviewCreateDTO {
    private UUID bookId;
    private UUID userId;
    private Integer rating;  // Expected 1 to 5
    private String reviewText;
}
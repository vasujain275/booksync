package me.vasujain.booksync.dto;

import lombok.Data;

@Data
public class BookReviewUpdateDTO {
    private Integer rating;  // Expected 1 to 5
    private String reviewText;
}
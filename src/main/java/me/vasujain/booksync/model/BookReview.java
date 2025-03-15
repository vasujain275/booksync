package me.vasujain.booksync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookReview {
    private UUID id;
    private UUID bookId;
    private UUID userId;
    private Integer rating; // 1 to 5
    private String reviewText;
    private LocalDateTime createdAt;
}

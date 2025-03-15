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
public class Book {
    private UUID id;
    private String isbn;
    private String title;
    private String authors;
    private String publishDate;
    private String publisher;
    private Integer coverId;
    private String firstSentence;
    private Integer numberOfPages;
    private String isbn10;
    private String isbn13;
    private String ocaid;
    private String workKey;
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
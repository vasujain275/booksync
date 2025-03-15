package me.vasujain.booksync.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private UUID id;
    private String title;
    private List<String> authors;
    private String description;
    private String publisher;
    private String publishedDate;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
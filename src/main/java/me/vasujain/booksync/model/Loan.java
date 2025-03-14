package me.vasujain.booksync.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Loan {
    private UUID id;
    private UUID userId;
    private UUID bookId;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
    private LocalDate returnedDate;
    private String status; // 'active', 'returned', 'overdue'
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package me.vasujain.booksync.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class LoanCreateDTO {
    private UUID userId;
    private UUID bookId;
    private LocalDate borrowedDate;
    private LocalDate dueDate;
}

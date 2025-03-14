package me.vasujain.booksync.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanUpdateDTO {
    private LocalDate returnedDate;
    // Expected values: "active", "returned", "overdue"
    private String status;
}
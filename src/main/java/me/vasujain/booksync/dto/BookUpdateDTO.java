package me.vasujain.booksync.dto;

import lombok.Data;

@Data
public class BookUpdateDTO {
    // Allow updating certain fields only.
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
}
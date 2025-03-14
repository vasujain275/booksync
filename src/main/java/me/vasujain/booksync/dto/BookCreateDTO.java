package me.vasujain.booksync.dto;

import lombok.Data;

@Data
public class BookCreateDTO {
    // For book creation only the ISBN is required.
    private String isbn;
}
package me.vasujain.booksync.dto;

import lombok.Data;
import java.util.List;

@Data
public class BookCreateDTO {
    private String title;
    private List<String> authors;
    private String description;
    private String publisher;
    private String publishedDate;
    private String category;
}
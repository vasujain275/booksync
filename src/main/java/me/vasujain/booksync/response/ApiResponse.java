package me.vasujain.booksync.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;
    private LocalDate timestamp;
    private PaginationMetadata pagination;

    @Data
    @Builder
    public static class PaginationMetadata {
        private int totalElements;
        private int totalPages;
        private int currentPage;
        private int pageSize;
    }
}

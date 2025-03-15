package me.vasujain.booksync.util;

import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import java.time.LocalDate;

public class ResponseUtil {

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .data(data)
                .timestamp(LocalDate.now())
                .build();
    }

    public static <T> ApiResponse<T> successPaginated(PaginatedResult<T> paginatedResult, String message) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK)
                .message(message)
                .timestamp(LocalDate.now())
                .data((T) paginatedResult.getContent())
                .pagination(ApiResponse.PaginationMetadata.builder()
                        .totalElements((int) paginatedResult.getTotalElements())
                        .totalPages(paginatedResult.getTotalPages())
                        .currentPage(paginatedResult.getCurrentPage())
                        .pageSize(paginatedResult.getPageSize())
                        .build())
                .build();
    }
}

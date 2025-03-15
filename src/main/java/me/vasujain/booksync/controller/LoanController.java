package me.vasujain.booksync.controller;

import me.vasujain.booksync.dto.ApiResponse;
import me.vasujain.booksync.dto.LoanCreateDTO;
import me.vasujain.booksync.dto.LoanUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.model.Loan;
import me.vasujain.booksync.service.LoanService;
import me.vasujain.booksync.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getLoans(@RequestParam(required = false, defaultValue = "false") boolean paginate,
                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                   @RequestParam(required = false, defaultValue = "10") int size,
                                                   @RequestParam(required = false, defaultValue = "borrowed_date") String sortBy,
                                                   @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        Object result = loanService.getAllLoans(paginate, page, size, sortBy, sortDirection);
        ApiResponse<?> response;
        if (paginate) {
            response = ResponseUtil.successPaginated((PaginatedResult<?>) result, "Loans retrieved successfully");
        } else {
            response = ResponseUtil.success(result, "Loans retrieved successfully");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Loan>> getLoanById(@PathVariable("id") UUID id) {
        Loan loan = loanService.getLoanById(id);
        ApiResponse<Loan> response = ResponseUtil.success(loan, "Loan retrieved successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Loan>> createLoan(@RequestBody LoanCreateDTO createDTO) {
        Loan loan = loanService.createLoan(createDTO);
        ApiResponse<Loan> response = ResponseUtil.success(loan, "Loan created successfully");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Loan>> updateLoan(@PathVariable("id") UUID id, @RequestBody LoanUpdateDTO updateDTO) {
        Loan loan = loanService.updateLoan(id, updateDTO);
        ApiResponse<Loan> response = ResponseUtil.success(loan, "Loan updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLoan(@PathVariable("id") UUID id) {
        loanService.deleteLoan(id);
        ApiResponse<Void> response = ResponseUtil.success(null, "Loan deleted successfully");
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }
}

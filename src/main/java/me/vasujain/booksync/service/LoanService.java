package me.vasujain.booksync.service;

import me.vasujain.booksync.dto.LoanCreateDTO;
import me.vasujain.booksync.dto.LoanUpdateDTO;
import me.vasujain.booksync.dto.PaginatedResult;
import me.vasujain.booksync.expection.ResourceNotFoundException;
import me.vasujain.booksync.model.Loan;
import me.vasujain.booksync.repository.LoanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepository;

    public LoanService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public Object getAllLoans(boolean paginate, int page, int size, String sortBy, String sortDir) {
        if (paginate) {
            int offset = page * size;
            List<Loan> loans = loanRepository.findAll(offset, size, sortBy, sortDir);
            int totalElements = loanRepository.countAll();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            return PaginatedResult.<Loan>builder()
                    .content(loans)
                    .currentPage(page)
                    .pageSize(size)
                    .totalElements(totalElements)
                    .totalPages(totalPages)
                    .build();
        } else {
            return loanRepository.findAll(0, Integer.MAX_VALUE, sortBy, sortDir);
        }
    }

    public Loan getLoanById(UUID id) {
        Optional<Loan> loanOpt = loanRepository.findById(id);
        if (loanOpt.isEmpty()) {
            throw new ResourceNotFoundException("Loan not found with id: " + id);
        }
        return loanOpt.get();
    }

    @Transactional
    public Loan createLoan(LoanCreateDTO createDTO) {
        Loan loan = Loan.builder()
                .id(UUID.randomUUID())
                .userId(createDTO.getUserId())
                .bookId(createDTO.getBookId())
                .borrowedDate(createDTO.getBorrowedDate())
                .dueDate(createDTO.getDueDate())
                .status("active")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        loanRepository.save(loan);
        return loan;
    }

    @Transactional
    public Loan updateLoan(UUID id, LoanUpdateDTO updateDTO) {
        Loan existingLoan = getLoanById(id);
        if (updateDTO.getReturnedDate() != null) {
            existingLoan.setReturnedDate(updateDTO.getReturnedDate());
        }
        if (updateDTO.getStatus() != null) {
            existingLoan.setStatus(updateDTO.getStatus());
        }
        existingLoan.setUpdatedAt(LocalDateTime.now());
        loanRepository.update(existingLoan);
        return existingLoan;
    }

    @Transactional
    public void deleteLoan(UUID id) {
        getLoanById(id);
        loanRepository.deleteById(id);
    }
}

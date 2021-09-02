package com.greve.libraryapi.service;

import com.greve.libraryapi.api.dto.LoanFilterDTO;
import com.greve.libraryapi.api.resource.BookController;
import com.greve.libraryapi.model.entity.Book;
import com.greve.libraryapi.model.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {
    Loan save(Loan loan);

    Optional<Loan> getById(Long id);

    Loan update(Loan loan);

    Page<Loan> find(LoanFilterDTO filter, Pageable pageable);

    Page<Loan> getLoansByBook(Book book, Pageable pageable);
}

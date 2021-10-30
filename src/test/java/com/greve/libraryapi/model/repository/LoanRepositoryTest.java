package com.greve.libraryapi.model.repository;

import com.greve.libraryapi.model.entity.Book;
import com.greve.libraryapi.model.entity.Loan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

import static com.greve.libraryapi.model.repository.BookRepositoryTest.createNewBook;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("Deve verificar se existe emprestimo não devolvido para o livro")
    public void existsByBookAndNotReturnedTest() {
        //cenario
        Loan loan = createAndPersistLoan(LocalDate.now());
        Book book = loan.getBook();

        //execução
        boolean exists = repository.existsByBookAndNotReturned(book);

        //verificação
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve buscar empréstimos pelo isbn do livro ou customer")
    public void findByIsbnOrCustomerTest() {
        Loan loan = createAndPersistLoan(LocalDate.now());

        Page<Loan> result = repository.findByBookIsbnOrCustomer("123", "Fulano", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).contains(loan);
        assertThat(result.getPageable().getPageSize()).isEqualTo(10);
        assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Deve obter emprestimos cuja data emprestimo for menor ou igual a tres dias atras e nao retornados")
    public void findByLoanDateLessThanAndNotReturnedTest() {
        Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));

        List<Loan> result = repository.findbyLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).hasSize(1).contains(loan);
    }

    @Test
    @DisplayName("Deve retornar vazio quando não houver emprestimos atrasados")
    public void notFindByLoanDateLessThanAndNotReturnedTest() {
        Loan loan = createAndPersistLoan(LocalDate.now());

        List<Loan> result = repository.findbyLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));

        assertThat(result).isEmpty();
    }

    public Loan createAndPersistLoan(LocalDate loanDate) {
        Book book = createNewBook("321");
        entityManager.persist(book);

        Loan loan = Loan.builder().book(book).customer("Fulano").loanDate(loanDate).build();
        entityManager.persist(loan);

        return loan;
    }

}

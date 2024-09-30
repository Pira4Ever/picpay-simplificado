package edu.octavio.simplified_picpay.domain.repository;

import edu.octavio.simplified_picpay.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}

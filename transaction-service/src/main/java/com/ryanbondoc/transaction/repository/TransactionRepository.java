package com.ryanbondoc.transaction.repository;

import com.ryanbondoc.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Custom query to find a transaction by its unique transaction ID
    Optional<Transaction> findByTransactionId(String transactionId);

    // You can add more custom queries as needed (e.g., based on status, timestamp, etc.)
}

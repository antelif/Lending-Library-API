package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** Transaction repository. */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {}

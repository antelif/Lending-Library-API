package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** TransactionEntity repository. */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {}

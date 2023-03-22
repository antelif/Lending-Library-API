package com.antelif.library.infrastructure.repository;

import com.antelif.library.infrastructure.entity.BookCopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** BookCopyEntity repository. */
@Repository
public interface BookCopyRepository extends JpaRepository<BookCopyEntity, Long> {

}

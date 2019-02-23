package com.example.demo.repository;

import com.example.demo.domain.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Transactional
@RepositoryRestResource(collectionResourceRel = "entry", path = "journal")
public interface JournalRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByCreatedAfter(@Param("after") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after);
    List<JournalEntry> findByCreatedBetween(@Param("after") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate after, @Param("before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate before);
    List<JournalEntry> findByTitleContaining(@Param("word") String word);
    List<JournalEntry> findBySummaryContaining(@Param("word")String word);
}

package com.fy.incometracker.repository;

import com.fy.incometracker.entity.IncomeEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeEntryRepository extends JpaRepository<IncomeEntry, Long> {
}

package com.fy.incometracker.repository;

import com.fy.incometracker.entity.IncomeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeTypeRepository extends JpaRepository<IncomeType, Long> {
}

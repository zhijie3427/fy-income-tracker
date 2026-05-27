package com.fy.incometracker.repository;

import com.fy.incometracker.entity.IncomeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeRecordRepository extends JpaRepository<IncomeRecord, Long> {
    List<IncomeRecord> findByUserIdAndRecordDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    
    List<IncomeRecord> findByAccountIdAndRecordDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT r FROM IncomeRecord r WHERE r.userId = :userId AND r.recordDate = :date")
    List<IncomeRecord> findByUserIdAndRecordDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Query("SELECT r FROM IncomeRecord r WHERE r.accountId = :accountId AND r.recordDate = :date")
    IncomeRecord findByAccountIdAndRecordDate(@Param("accountId") Long accountId, @Param("date") LocalDate date);
}

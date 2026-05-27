package com.fy.incometracker.repository;

import com.fy.incometracker.entity.MoneyItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyItemRepository extends JpaRepository<MoneyItem, Long> {
}

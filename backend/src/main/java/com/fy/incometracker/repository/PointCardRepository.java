package com.fy.incometracker.repository;

import com.fy.incometracker.entity.PointCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointCardRepository extends JpaRepository<PointCard, Long> {
}

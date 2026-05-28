package com.fy.incometracker.repository;

import com.fy.incometracker.entity.Faction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionRepository extends JpaRepository<Faction, Long> {
}

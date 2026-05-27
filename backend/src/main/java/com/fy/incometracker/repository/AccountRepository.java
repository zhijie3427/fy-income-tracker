package com.fy.incometracker.repository;

import com.fy.incometracker.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
    Account findByUserIdAndAccountIndex(Long userId, Integer accountIndex);
}

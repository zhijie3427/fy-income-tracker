package com.fy.incometracker.service;

import com.fy.incometracker.entity.Account;
import com.fy.incometracker.repository.AccountRepository;
import com.fy.incometracker.repository.FactionRepository;
import com.fy.incometracker.entity.Faction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final FactionRepository factionRepository;

    public Account createAccount(Account account) {
        Account saved = accountRepository.save(account);
        if (saved.getSectId() != null) {
            factionRepository.findById(saved.getSectId()).ifPresent(f -> saved.setSectName(f.getName()));
        }
        return saved;
    }

    public List<Account> getAllAccounts() {
        List<Account> list = accountRepository.findAll();
        List<Faction> factions = factionRepository.findAll();
        final java.util.Map<Long, String> sectMap = new java.util.HashMap<>();
        factions.forEach(f -> sectMap.put(f.getId(), f.getName()));
        list.forEach(a -> {
            if (a.getSectId() != null) {
                a.setSectName(sectMap.get(a.getSectId()));
            }
        });
        return list;
    }

    public Account updateAccount(Account account) {
        Account saved = accountRepository.save(account);
        if (saved.getSectId() != null) {
            factionRepository.findById(saved.getSectId()).ifPresent(f -> saved.setSectName(f.getName()));
        }
        return saved;
    }

    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    public Account getAccountById(Long accountId) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("账号不存在"));
        if (acc.getSectId() != null) {
            factionRepository.findById(acc.getSectId()).ifPresent(f -> acc.setSectName(f.getName()));
        }
        return acc;
    }
}

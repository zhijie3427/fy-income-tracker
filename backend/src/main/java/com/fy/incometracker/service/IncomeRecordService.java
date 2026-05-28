package com.fy.incometracker.service;

import com.fy.incometracker.dto.AccountIncomeStatDTO;
import com.fy.incometracker.dto.TypeIncomeStatDTO;
import com.fy.incometracker.entity.IncomeEntry;
import com.fy.incometracker.entity.IncomeRecord;
import com.fy.incometracker.repository.IncomeRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class IncomeRecordService {

    private final IncomeRecordRepository incomeRecordRepository;

    public IncomeRecord createRecord(IncomeRecord record) {
        if (record.getUserId() == null) {
            record.setUserId(1L);
        }
        bindChildren(record);
        recalculateTotals(record);
        return incomeRecordRepository.save(record);
    }

    public IncomeRecord updateRecord(IncomeRecord record) {
        if (record.getUserId() == null) {
            record.setUserId(1L);
        }
        bindChildren(record);
        recalculateTotals(record);
        return incomeRecordRepository.save(record);
    }

    private void bindChildren(IncomeRecord record) {
        if (record.getIncomeEntries() == null) {
            return;
        }
        record.getIncomeEntries().forEach(entry -> {
            entry.setIncomeRecord(record);
            if (entry.getItemRewards() != null) {
                entry.getItemRewards().forEach(item -> item.setIncomeEntry(entry));
            }
        });
    }

    private void recalculateTotals(IncomeRecord record) {
        BigDecimal totalIncome = BigDecimal.ZERO;
        if (record.getIncomeEntries() != null) {
            for (IncomeEntry entry : record.getIncomeEntries()) {
                BigDecimal entryMoney = entry.getMoney() == null ? BigDecimal.ZERO : entry.getMoney();
                totalIncome = totalIncome.add(entryMoney);

                if (entry.getItemRewards() != null) {
                    for (var item : entry.getItemRewards()) {
                        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity() == null ? 1 : item.getQuantity());
                        BigDecimal itemMoney = item.getMoney() == null ? BigDecimal.ZERO : item.getMoney();
                        totalIncome = totalIncome.add(itemMoney.multiply(quantity));
                    }
                }
            }
        }
        record.setTotalIncome(totalIncome);
        record.setNetIncome(record.getTotalIncome() == null ? BigDecimal.ZERO : record.getTotalIncome());
    }

    public List<IncomeRecord> getAllRecords() {
        return incomeRecordRepository.findAll();
    }

    public void deleteRecord(Long recordId) {
        incomeRecordRepository.deleteById(recordId);
    }

    public IncomeRecord getRecordById(Long recordId) {
        return incomeRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("记录不存在"));
    }

    public List<IncomeRecord> getUserRecordsBetween(Long userId, LocalDate startDate, LocalDate endDate) {
        return incomeRecordRepository.findByUserIdAndRecordDateBetween(userId, startDate, endDate);
    }

    public List<IncomeRecord> getAccountRecordsBetween(Long accountId, LocalDate startDate, LocalDate endDate) {
        return incomeRecordRepository.findByAccountIdAndRecordDateBetween(accountId, startDate, endDate);
    }

    public List<IncomeRecord> getUserRecordsByDate(Long userId, LocalDate date) {
        return incomeRecordRepository.findByUserIdAndRecordDate(userId, date);
    }

    public IncomeRecord getAccountRecordByDate(Long accountId, LocalDate date) {
        return incomeRecordRepository.findByAccountIdAndRecordDate(accountId, date);
    }

    // 统计相关方法
    public BigDecimal getTotalIncomeByUser(Long userId, LocalDate startDate, LocalDate endDate) {
        return getUserRecordsBetween(userId, startDate, endDate).stream()
                .map(IncomeRecord::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalNetIncomeByUser(Long userId, LocalDate startDate, LocalDate endDate) {
        return getUserRecordsBetween(userId, startDate, endDate).stream()
                .map(IncomeRecord::getNetIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalIncomeByAccount(Long accountId, LocalDate startDate, LocalDate endDate) {
        return getAccountRecordsBetween(accountId, startDate, endDate).stream()
                .map(IncomeRecord::getTotalIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<AccountIncomeStatDTO> getIncomeByAccount(LocalDate startDate, LocalDate endDate) {
        List<IncomeRecord> records = getAllRecords();
        if (startDate != null && endDate != null) {
            records = records.stream()
                    .filter(record -> !record.getRecordDate().isBefore(startDate) && !record.getRecordDate().isAfter(endDate))
                    .toList();
        }

        Map<Long, BigDecimal> accountTotals = new HashMap<>();
        records.forEach(record -> {
            Long accountId = record.getAccountId();
            BigDecimal total = record.getTotalIncome() != null ? record.getTotalIncome() : BigDecimal.ZERO;
            accountTotals.put(accountId, accountTotals.getOrDefault(accountId, BigDecimal.ZERO).add(total));
        });

        List<AccountIncomeStatDTO> stats = new ArrayList<>();
        accountTotals.forEach((accountId, totalIncome) -> stats.add(new AccountIncomeStatDTO(accountId, null, totalIncome)));
        return stats;
    }

    public List<TypeIncomeStatDTO> getIncomeByType(LocalDate startDate, LocalDate endDate) {
        List<IncomeRecord> records = getAllRecords();
        if (startDate != null && endDate != null) {
            records = records.stream()
                    .filter(record -> !record.getRecordDate().isBefore(startDate) && !record.getRecordDate().isAfter(endDate))
                    .toList();
        }

        Map<String, BigDecimal> typeTotals = new HashMap<>();
        for (IncomeRecord record : records) {
            if (record.getIncomeEntries() == null) {
                continue;
            }
            for (IncomeEntry entry : record.getIncomeEntries()) {
                String type = entry.getType() == null ? "未知" : entry.getType();
                BigDecimal money = entry.getMoney() == null ? BigDecimal.ZERO : entry.getMoney();
                typeTotals.put(type, typeTotals.getOrDefault(type, BigDecimal.ZERO).add(money));
            }
        }

        List<TypeIncomeStatDTO> stats = new ArrayList<>();
        typeTotals.forEach((type, totalIncome) -> stats.add(new TypeIncomeStatDTO(type, totalIncome)));
        return stats;
    }
}

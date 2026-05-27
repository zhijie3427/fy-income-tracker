package com.fy.incometracker.controller;

import com.fy.incometracker.dto.AccountIncomeStatDTO;
import com.fy.incometracker.dto.TypeIncomeStatDTO;
import com.fy.incometracker.entity.IncomeRecord;
import com.fy.incometracker.service.AccountService;
import com.fy.incometracker.service.IncomeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/income-records", "/api/income-records"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IncomeRecordController {

    private final IncomeRecordService incomeRecordService;
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<IncomeRecord> createRecord(@RequestBody IncomeRecord record) {
        IncomeRecord created = incomeRecordService.createRecord(record);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<IncomeRecord> getRecord(@PathVariable Long recordId) {
        IncomeRecord record = incomeRecordService.getRecordById(recordId);
        return ResponseEntity.ok(record);
    }

    @PutMapping("/{recordId}")
    public ResponseEntity<IncomeRecord> updateRecord(@PathVariable Long recordId, @RequestBody IncomeRecord record) {
        record.setId(recordId);
        IncomeRecord updated = incomeRecordService.updateRecord(record);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long recordId) {
        incomeRecordService.deleteRecord(recordId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<IncomeRecord>> getRecords(
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<IncomeRecord> records = incomeRecordService.getAllRecords();
        if (accountId != null) {
            records = records.stream()
                    .filter(record -> record.getAccountId().equals(accountId)).toList();
        }
        if (startDate != null && endDate != null) {
            records = records.stream()
                    .filter(record -> !record.getRecordDate().isBefore(startDate) && !record.getRecordDate().isAfter(endDate))
                    .toList();
        }
        return ResponseEntity.ok(records);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeRecord>> getUserRecords(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<IncomeRecord> records = incomeRecordService.getUserRecordsBetween(userId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<IncomeRecord>> getAccountRecords(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<IncomeRecord> records = incomeRecordService.getAccountRecordsBetween(accountId, startDate, endDate);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/user/{userId}/date/{date}")
    public ResponseEntity<List<IncomeRecord>> getUserRecordsByDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<IncomeRecord> records = incomeRecordService.getUserRecordsByDate(userId, date);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/statistics/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserStatistics(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        BigDecimal totalIncome = incomeRecordService.getTotalIncomeByUser(userId, startDate, endDate);
        BigDecimal totalNetIncome = incomeRecordService.getTotalNetIncomeByUser(userId, startDate, endDate);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIncome", totalIncome);
        stats.put("totalNetIncome", totalNetIncome);
        stats.put("startDate", startDate);
        stats.put("endDate", endDate);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/statistics/by-account")
    public ResponseEntity<List<AccountIncomeStatDTO>> getIncomeByAccount(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<AccountIncomeStatDTO> stats = incomeRecordService.getIncomeByAccount(startDate, endDate);
        var accounts = accountService.getAllAccounts();
        var accountNameMap = accounts.stream().collect(java.util.stream.Collectors.toMap(a -> a.getId(), a -> a.getAccountName()));
        stats.forEach(stat -> stat.setAccountName(accountNameMap.getOrDefault(stat.getAccountId(), "未知账号")));
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/statistics/by-type")
    public ResponseEntity<List<TypeIncomeStatDTO>> getIncomeByType(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(incomeRecordService.getIncomeByType(startDate, endDate));
    }
}

package com.fy.incometracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountIncomeStatDTO {
    private Long accountId;
    private String accountName;
    private BigDecimal totalIncome;
}

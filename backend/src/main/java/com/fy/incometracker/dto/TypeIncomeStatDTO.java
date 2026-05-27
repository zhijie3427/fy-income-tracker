package com.fy.incometracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeIncomeStatDTO {
    private String type;
    private BigDecimal totalIncome;
}

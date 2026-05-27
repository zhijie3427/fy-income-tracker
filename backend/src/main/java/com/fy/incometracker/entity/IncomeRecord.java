package com.fy.incometracker.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fy.incometracker.entity.IncomeEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "income_records")
public class IncomeRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private LocalDate recordDate;  // 记录日期

    // 收益相关
    @Column(precision = 15, scale = 2)
    private BigDecimal totalIncome;  // 总收益

    @Column(precision = 15, scale = 2)
    private BigDecimal dungeonIncome;  // 副本收益

    @Column(precision = 15, scale = 2)
    private BigDecimal stallIncome;  // 摆摊收益

    @Column(precision = 15, scale = 2)
    private BigDecimal taskIncome;  // 任务收益

    @Column(precision = 15, scale = 2)
    private BigDecimal otherIncome;  // 其他收益

    // 子项：具体收益明细
    @JsonProperty("incomeRecords")
    @JsonManagedReference
    @OneToMany(mappedBy = "incomeRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeEntry> incomeEntries = new ArrayList<>();

    // 投入成本（保留字段，前端可不传）
    @Column(precision = 15, scale = 2)
    private BigDecimal itemCost;  // 物品消耗成本

    @Column(precision = 15, scale = 2)
    private BigDecimal goldCost;  // 金币消耗

    @Column(precision = 15, scale = 2)
    private BigDecimal otherCost;  // 其他成本

    @Column(precision = 15, scale = 2)
    private BigDecimal netIncome;  // 净收益 = totalIncome - itemCost - goldCost - otherCost

    private Integer timeSpent;  // 花费时间（分钟）

    private String notes;  // 备注

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculateNetIncome();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculateNetIncome();
    }

    private void calculateNetIncome() {
        BigDecimal totalCost = BigDecimal.ZERO;
        if (itemCost != null) totalCost = totalCost.add(itemCost);
        if (goldCost != null) totalCost = totalCost.add(goldCost);
        if (otherCost != null) totalCost = totalCost.add(otherCost);

        // 如果没有 totalIncome，则尝试从 incomeEntries 汇总
        if (totalIncome == null || BigDecimal.ZERO.compareTo(totalIncome) == 0) {
            BigDecimal sum = BigDecimal.ZERO;
            if (incomeEntries != null) {
                for (IncomeEntry ie : incomeEntries) {
                    if (ie.getMoney() != null) sum = sum.add(ie.getMoney());
                }
            }
            totalIncome = sum;
        }

        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        this.netIncome = totalIncome.subtract(totalCost == null ? BigDecimal.ZERO : totalCost);
    }
}


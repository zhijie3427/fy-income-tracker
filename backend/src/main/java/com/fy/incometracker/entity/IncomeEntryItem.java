package com.fy.incometracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "income_entry_items")
public class IncomeEntryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long itemId;
    private String itemName;
    private String itemType;
    private Integer level;
    private Integer quantity;

    @Column(precision = 15, scale = 2)
    private BigDecimal money;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_entry_id")
    @JsonBackReference
    private IncomeEntry incomeEntry;
}

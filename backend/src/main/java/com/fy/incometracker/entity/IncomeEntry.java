package com.fy.incometracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "income_entries")
public class IncomeEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String detail;
    private String activityType;
    private Long activityTypeId;
    private Long itemId;
    private String itemName;
    private Integer level;
    private Integer quantity;
    private Integer experience;
    private String startTime;
    private String endTime;

    @Column(precision = 15, scale = 2)
    private BigDecimal money;

    @Builder.Default
    @JsonManagedReference
    @OneToMany(mappedBy = "incomeEntry", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncomeEntryItem> itemRewards = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "income_record_id")
    @JsonBackReference
    private IncomeRecord incomeRecord;
}

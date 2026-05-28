package com.fy.incometracker.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String accountName;  // 角色名

    @Column(columnDefinition = "int default 1")
    private Integer level;  // 等级

    @Column(columnDefinition = "bigint default 0")
    private Long currentExperience; // 当前经验

    @Column(columnDefinition = "bigint default 0")
    private Long startingMoney; // 所带金钱（两）

    @Column
    private Long sectId; // 门派ID，关联 factions 表

    @Column(columnDefinition = "varchar(255) default '正常'")
    private String status;  // 正常/休息/暂停

    private String notes;  // 备注

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sectName; // 非持久化字段，用于返回给前端的门派名称

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}

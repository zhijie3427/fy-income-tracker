package com.fy.incometracker.controller;

import com.fy.incometracker.entity.PointCard;
import com.fy.incometracker.entity.IncomeType;
import com.fy.incometracker.entity.MoneyItem;
import com.fy.incometracker.service.PointCardService;
import com.fy.incometracker.service.IncomeTypeService;
import com.fy.incometracker.service.MoneyItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SystemMaintenanceController {

    private final PointCardService pointCardService;
    private final IncomeTypeService incomeTypeService;
    private final MoneyItemService moneyItemService;

    // ========== 点卡信息相关接口 ==========

    @GetMapping("/point-card")
    public ResponseEntity<PointCard> getPointCard() {
        PointCard pointCard = pointCardService.getPointCard();
        if (pointCard == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(pointCard);
    }

    @PostMapping("/point-card")
    public ResponseEntity<PointCard> savePointCard(@RequestBody PointCard pointCard) {
        PointCard saved = pointCardService.savePointCard(pointCard);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/point-card/{id}")
    public ResponseEntity<Void> deletePointCard(@PathVariable Long id) {
        pointCardService.deletePointCard(id);
        return ResponseEntity.noContent().build();
    }

    // ========== 收益类型相关接口 ==========

    @PostMapping("/income-types")
    public ResponseEntity<IncomeType> createIncomeType(@RequestBody IncomeType incomeType) {
        IncomeType created = incomeTypeService.createIncomeType(incomeType);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/income-types")
    public ResponseEntity<List<IncomeType>> getAllIncomeTypes() {
        List<IncomeType> incomeTypes = incomeTypeService.getAllIncomeTypes();
        return ResponseEntity.ok(incomeTypes);
    }

    @GetMapping("/income-types/{id}")
    public ResponseEntity<IncomeType> getIncomeTypeById(@PathVariable Long id) {
        IncomeType incomeType = incomeTypeService.getIncomeTypeById(id);
        return ResponseEntity.ok(incomeType);
    }

    @PutMapping("/income-types/{id}")
    public ResponseEntity<IncomeType> updateIncomeType(@PathVariable Long id, @RequestBody IncomeType incomeType) {
        IncomeType updated = incomeTypeService.updateIncomeType(id, incomeType);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/income-types/{id}")
    public ResponseEntity<Void> deleteIncomeType(@PathVariable Long id) {
        incomeTypeService.deleteIncomeType(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/income-types/batch")
    public ResponseEntity<Void> saveAllIncomeTypes(@RequestBody List<IncomeType> incomeTypes) {
        incomeTypeService.saveAllIncomeTypes(incomeTypes);
        return ResponseEntity.ok().build();
    }

    // ========== 物品相关接口 ==========

    @PostMapping("/money-items")
    public ResponseEntity<MoneyItem> createMoneyItem(@RequestBody MoneyItem moneyItem) {
        MoneyItem created = moneyItemService.createMoneyItem(moneyItem);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/money-items")
    public ResponseEntity<List<MoneyItem>> getAllMoneyItems() {
        List<MoneyItem> moneyItems = moneyItemService.getAllMoneyItems();
        return ResponseEntity.ok(moneyItems);
    }

    @GetMapping("/money-items/{id}")
    public ResponseEntity<MoneyItem> getMoneyItemById(@PathVariable Long id) {
        MoneyItem moneyItem = moneyItemService.getMoneyItemById(id);
        return ResponseEntity.ok(moneyItem);
    }

    @PutMapping("/money-items/{id}")
    public ResponseEntity<MoneyItem> updateMoneyItem(@PathVariable Long id, @RequestBody MoneyItem moneyItem) {
        MoneyItem updated = moneyItemService.updateMoneyItem(id, moneyItem);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/money-items/{id}")
    public ResponseEntity<Void> deleteMoneyItem(@PathVariable Long id) {
        moneyItemService.deleteMoneyItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/money-items/batch")
    public ResponseEntity<Void> saveAllMoneyItems(@RequestBody List<MoneyItem> moneyItems) {
        moneyItemService.saveAllMoneyItems(moneyItems);
        return ResponseEntity.ok().build();
    }
}

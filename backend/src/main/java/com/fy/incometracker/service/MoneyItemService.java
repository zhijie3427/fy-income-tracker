package com.fy.incometracker.service;

import com.fy.incometracker.entity.MoneyItem;
import com.fy.incometracker.repository.MoneyItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoneyItemService {

    private final MoneyItemRepository moneyItemRepository;

    public MoneyItem createMoneyItem(MoneyItem moneyItem) {
        return moneyItemRepository.save(moneyItem);
    }

    public List<MoneyItem> getAllMoneyItems() {
        return moneyItemRepository.findAll();
    }

    public MoneyItem getMoneyItemById(Long id) {
        return moneyItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物品不存在"));
    }

    public MoneyItem updateMoneyItem(Long id, MoneyItem moneyItem) {
        MoneyItem existing = getMoneyItemById(id);
        existing.setItemType(moneyItem.getItemType());
        existing.setItemName(moneyItem.getItemName());
        existing.setMoneyValue(moneyItem.getMoneyValue());
        return moneyItemRepository.save(existing);
    }

    public void deleteMoneyItem(Long id) {
        moneyItemRepository.deleteById(id);
    }

    public void saveAllMoneyItems(List<MoneyItem> moneyItems) {
        moneyItemRepository.saveAll(moneyItems);
    }
}

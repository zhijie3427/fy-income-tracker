package com.fy.incometracker.service;

import com.fy.incometracker.entity.IncomeType;
import com.fy.incometracker.repository.IncomeTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeTypeService {

    private final IncomeTypeRepository incomeTypeRepository;

    public IncomeType createIncomeType(IncomeType incomeType) {
        return incomeTypeRepository.save(incomeType);
    }

    public List<IncomeType> getAllIncomeTypes() {
        return incomeTypeRepository.findAll();
    }

    public IncomeType getIncomeTypeById(Long id) {
        return incomeTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("收益类型不存在"));
    }

    public IncomeType updateIncomeType(Long id, IncomeType incomeType) {
        IncomeType existing = getIncomeTypeById(id);
        existing.setType(incomeType.getType());
        existing.setTypeName(incomeType.getTypeName());
        return incomeTypeRepository.save(existing);
    }

    public void deleteIncomeType(Long id) {
        incomeTypeRepository.deleteById(id);
    }

    public void saveAllIncomeTypes(List<IncomeType> incomeTypes) {
        incomeTypeRepository.saveAll(incomeTypes);
    }
}

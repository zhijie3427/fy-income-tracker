package com.fy.incometracker.service;

import com.fy.incometracker.entity.Faction;
import com.fy.incometracker.repository.FactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FactionService {

    private final FactionRepository factionRepository;

    public List<Faction> getAllFactions() {
        return factionRepository.findAll();
    }

    public Faction createFaction(Faction faction) {
        return factionRepository.save(faction);
    }
}

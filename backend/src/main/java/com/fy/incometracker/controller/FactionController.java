package com.fy.incometracker.controller;

import com.fy.incometracker.entity.Faction;
import com.fy.incometracker.service.FactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FactionController {

    private final FactionService factionService;

    @GetMapping
    public ResponseEntity<List<Faction>> getAll() {
        return ResponseEntity.ok(factionService.getAllFactions());
    }

    @PostMapping
    public ResponseEntity<Faction> create(@RequestBody Faction faction) {
        return ResponseEntity.ok(factionService.createFaction(faction));
    }
}

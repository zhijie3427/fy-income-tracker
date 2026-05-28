package com.fy.incometracker.utils;

import com.fy.incometracker.entity.Faction;
import com.fy.incometracker.repository.FactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FactionRepository factionRepository;

    @Override
    public void run(String... args) throws Exception {
        if (factionRepository.count() == 0) {
            List<String> factions = Arrays.asList(
                    "大唐官府",
                    "化生寺",
                    "龙宫",
                    "方寸山",
                    "普陀山",
                    "五庄观",
                    "天宫",
                    "普陀",
                    "地府",
                    "盘丝洞",
                    "狮驼岭",
                    "神木林",
                    "无底洞",
                    "女儿村",
                    "凌波城"
            );
            factions.forEach(name -> {
                Faction f = Faction.builder().name(name).build();
                factionRepository.save(f);
            });
        }
    }
}

package com.fy.incometracker.service;

import com.fy.incometracker.entity.PointCard;
import com.fy.incometracker.repository.PointCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointCardService {

    private final PointCardRepository pointCardRepository;

    public PointCard getPointCard() {
        List<PointCard> pointCards = pointCardRepository.findAll();
        if (pointCards.isEmpty()) {
            return null;
        }
        return pointCards.get(0);
    }

    public PointCard savePointCard(PointCard pointCard) {
        List<PointCard> existingCards = pointCardRepository.findAll();
        if (!existingCards.isEmpty()) {
            pointCard.setId(existingCards.get(0).getId());
        }
        return pointCardRepository.save(pointCard);
    }

    public void deletePointCard(Long id) {
        pointCardRepository.deleteById(id);
    }
}

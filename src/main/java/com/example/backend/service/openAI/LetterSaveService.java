package com.example.backend.service.openAI;

import com.example.backend.entity.openAI.LetterSave;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import com.example.backend.repository.openAI.LetterSaveRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterSaveService {

    private final LetterSaveRepository letterSaveRepository;

    public void saveLetter(Store store, Order order, String letter) {
        LetterSave letterSave = new LetterSave();
        letterSave.setStore(store);
        letterSave.setOrder(order);
        letterSave.setMessageContent(letter);
        letterSave.setCreatedAt(LocalDateTime.now());
        letterSave.setSentimentScore(0);
        letterSave.setSentimentLabel("NEUTRAL");
        letterSaveRepository.save(letterSave);

    }

    public String getLetter(Order order) {
        return letterSaveRepository.findByOrderId(order.getId()).getMessageContent().toString();
    }
}

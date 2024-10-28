package com.example.backend.service.openAI;

import com.example.backend.dto.openAI.LetterSaveDTO;
import com.example.backend.entity.openAI.LetterSave;
import com.example.backend.entity.order.UserOrder;
import com.example.backend.entity.store.Store;
import com.example.backend.repository.openAI.LetterSaveRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LetterSaveService {

    private final LetterSaveRepository letterSaveRepository;

    public void saveLetter(Store store, UserOrder userOrder, String letter) {
        LetterSave letterSave = new LetterSave();
        letterSave.setStore(store);
        letterSave.setUserOrder(userOrder);
        letterSave.setMessageContent(letter);
        letterSave.setCreatedAt(LocalDateTime.now());
        letterSave.setSentimentScore(0);
        letterSave.setSentimentLabel("NEUTRAL");
        letterSaveRepository.save(letterSave);

    }

    public LetterSaveDTO getLetter(Long orderId) {
        LetterSave letterSave = letterSaveRepository.findByOrderId(orderId);
        if (letterSave == null) {
            return null;
        }

        LetterSaveDTO dto = new LetterSaveDTO();
        dto.setOrderId(letterSave.getId());
        dto.setCreatedAt(letterSave.getCreatedAt().toString());
        dto.setMessageContent(letterSave.getMessageContent());
        dto.setSentimentLabel(letterSave.getSentimentLabel());
        dto.setSentimentScore(letterSave.getSentimentScore());
        dto.setStoreId(letterSave.getStore().getId());

        return dto;
    }
}

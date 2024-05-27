package com.example.backend.dto.openAI;

import com.example.backend.entity.openAI.LetterSave;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LetterSaveDTO {
    private Long orderId;
    private Long userAccountId;
    private String messageContent;
    private float sentimentScore;
    private String sentimentLabel;
    private LocalDateTime createdAt;

    public LetterSave toEntity() {
        return LetterSave.builder()
                .id(orderId)
                .messageContent(messageContent)
                .sentimentScore(sentimentScore)
                .sentimentLabel(sentimentLabel)
                .createdAt(createdAt)
                .build();
    }
}

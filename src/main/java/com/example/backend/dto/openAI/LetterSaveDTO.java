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
    private String createdAt;
    private String messageContent;
    private String sentimentLabel;
    private Float sentimentScore;
    private Long storeId;

}

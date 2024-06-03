package com.example.backend.dto.openAI;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplySaveDTO {
    private Long reviewId;
    private String createdAt;
    private String replyContent;
    private String sentimentLabel;
    private Float sentimentScore;
    private Long storeId;
}

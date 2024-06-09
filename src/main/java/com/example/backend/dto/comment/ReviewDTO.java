package com.example.backend.dto.comment;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long orderId;
    private float rating;
    private String comment;
    private String nickName;
    private Long userId;
    private Long storeId;
    private List<ReplyResponseDTO> replies;
}

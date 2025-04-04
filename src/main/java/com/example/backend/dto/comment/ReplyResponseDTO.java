package com.example.backend.dto.comment;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponseDTO {
    private Long id;  // 리뷰의 고유 ID
    private String comment;  // 리뷰 내용
    private String nickName; //사용자 닉네임
    private LocalDateTime createAt;  // 리뷰 작성 시간
    private LocalDateTime updateAt;  // 리뷰 수정 시간
    private LocalDateTime deleteAt;  // 리뷰 삭제 시간
    private Long userId;  // 사용자 ID
    private Long reviewId;  // 리뷰 ID
}

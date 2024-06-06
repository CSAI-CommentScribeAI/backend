package com.example.backend.dto.comment;

import com.example.backend.dto.store.StoreDTO;
import com.example.backend.dto.userAccount.UserAccountRequestDTO;
import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDTO {
    private float rating;
    private String comment;
}

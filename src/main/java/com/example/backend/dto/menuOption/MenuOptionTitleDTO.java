package com.example.backend.dto.menuOption;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuOptionTitleDTO {
    private Long id;
    private String titleName;
    private boolean multipleCheck;
}

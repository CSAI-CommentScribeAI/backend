package com.example.backend.dto.menuOption;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MenuOptionDTO {
    private Long menuOption_id;
    private String optionName;
    private int optionPrice;
    private Long menuOptionTitleId;
}

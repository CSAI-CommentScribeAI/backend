package com.example.backend.dto.menu;

import com.example.backend.entity.menu.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequestDTO {
    private String name;
    private String imageUrl;
    private int price;
    private String menuDetail;
    private MenuStatus status;
    private Long storeId;
}

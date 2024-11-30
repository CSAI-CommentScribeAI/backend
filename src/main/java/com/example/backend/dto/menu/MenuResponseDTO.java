package com.example.backend.dto.menu;

import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.menu.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDTO {
    private Long id;
    private Long storeId;
    private String name;
    private String imageUrl;
    private int price;
    private String menuDetail;
    private MenuStatus status;

    public static MenuResponseDTO fromEntity(Menu m) {
        return MenuResponseDTO.builder()
                .id(m.getId())
                .storeId(m.getStore().getId())
                .name(m.getName())
                .imageUrl(m.getImageUrl())
                .price(m.getPrice())
                .status(m.getStatus())
                .menuDetail(m.getMenuDetail())
                .build();
    }
}

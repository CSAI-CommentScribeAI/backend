package com.example.backend.dto.menu;

import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.menu.MenuStatus;
import com.example.backend.entity.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private Long id;
    private Long storeId;
    private String name;
    private String imageUrl;
    private int price;
    private String menuDetail;
    private MenuStatus status;

    public static MenuDTO entityToDTO(Menu m) {
        return MenuDTO.builder()
                .id(m.getId())
                .storeId(m.getStore().getId())
                .name(m.getName())
                .imageUrl(m.getImageUrl())
                .price(m.getPrice())
                .status(m.getStatus())
                .menuDetail(m.getMenuDetail())
                .build();
    }
    public static List<MenuDTO> entityToDTO(List<Menu> menus){
        return menus
                .stream()
                .map(MenuDTO::entityToDTO)
                .collect(Collectors.toList());
    }
}

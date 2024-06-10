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
public class MenuListDTO {
    private Long storeId;
    private String name;
    private String menuUrl;
    private int price;
    private String menuDetail;


}

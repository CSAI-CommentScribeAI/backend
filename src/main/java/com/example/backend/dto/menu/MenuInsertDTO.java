package com.example.backend.dto.menu;

import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.menu.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuInsertDTO {
    private String name;
    private int price;
    private String menuDetail;
    private String imageUrl; // 이미지를 받아들이는 필드로 변경
    private MenuStatus status;
    public Menu dtoToEntity(){
        return Menu.builder()
                .name(name)
                .price(price)
                .menuDetail(menuDetail)
                .imageUrl(imageUrl)
                .status(status)
                .build();
    }


}
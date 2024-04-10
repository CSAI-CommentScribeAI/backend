package com.example.backend.menu.dto;

import com.example.backend.menu.entity.DiscountType;
import com.example.backend.menu.entity.Food;
import com.example.backend.menu.entity.FoodStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class FoodDTO {
    private Long id;
    private String name;
    private String shortDescription;
    private int price;
    private DiscountType discountType;
    private int discountAmount;
    private FoodStatus status;
    private String imageUrl;

    // 이미지 파일을 저장하는 MultipartFile 필드
    private MultipartFile foodImage;

    public FoodDTO(Food food) {
        this.id = food.getId();
        this.name = food.getName();
        this.shortDescription = food.getShortDescription();
        this.price = food.getPrice();
        this.discountType = food.getDiscountType();
        this.discountAmount = food.getDiscountAmount();
        this.status = food.getStatus();
        this.imageUrl = food.getFoodImage();
    }

}

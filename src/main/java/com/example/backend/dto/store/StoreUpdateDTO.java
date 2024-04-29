package com.example.backend.dto.store;

import com.example.backend.entity.store.Category;
import com.example.backend.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreUpdateDTO {
    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액
    private String storeImageUrl; // 배경 이미지
    private Category category;
    private String info;

    public static StoreUpdateDTO entityToDTO(Store s){
        return StoreUpdateDTO.builder()
                .name(s.getName())
                .minOrderPrice(s.getMinOrderPrice())
                .storeImageUrl(s.getStoreImageUrl())
                .category(s.getCategory())
                .info(s.getInfo())
                .build();
    }
}

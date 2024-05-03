package com.example.backend.dto.store;

import com.example.backend.entity.store.Category;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.store.StoreAddress;
import com.example.backend.entity.userAccount.UserAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSearchResponseDTO {
    private Long id;
    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액
    private String storeImageUrl; // 배경 이미지
    private Category category;
    private String info;
    private StoreAddress storeAddress;

    public static StoreSearchResponseDTO entityToDTO(Store s){
        return StoreSearchResponseDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .minOrderPrice(s.getMinOrderPrice())
                .category(s.getCategory())
                .info(s.getInfo())
                .storeAddress(s.getStoreAddress())
                .storeImageUrl(s.getStoreImageUrl())
                .build();
    }
}

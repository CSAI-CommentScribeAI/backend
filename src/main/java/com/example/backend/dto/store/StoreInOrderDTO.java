package com.example.backend.dto.store;

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
public class StoreInOrderDTO {
    private Long id;
    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액
    private String StoreImageUrl; // 배경 이미지
    private StoreAddress storeAddress;

    public static StoreInOrderDTO entityToDTO(Store store){
        return StoreInOrderDTO.builder()
                .id(store.getId())
                .name(store.getName())
                .minOrderPrice(store.getMinOrderPrice())
                .StoreImageUrl(store.getStoreImageUrl())
                .storeAddress(store.getStoreAddress())
                .build();
    }
}

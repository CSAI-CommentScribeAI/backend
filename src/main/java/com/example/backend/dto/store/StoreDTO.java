package com.example.backend.dto.store;

import com.example.backend.dto.menu.MenuDTO;
import com.example.backend.entity.store.Category;
import com.example.backend.entity.store.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {
    private Long id;
    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액
    private String storeImageUrl; // 배경 이미지
    private Category category;
    private String info;
    private StoreAddressDTO storeAddress; // 거리 정보
    private String businessLicense; // 사업자 등록증
    private Long userId; // UserAccount 엔티티의 id만 저장

    @Builder.Default
    private List<MenuDTO> menus = new ArrayList<>();

    public static StoreDTO entityToDTO(Store s) {
        return StoreDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .minOrderPrice(s.getMinOrderPrice())
                .category(s.getCategory())
                .info(s.getInfo())
                .storeAddress(StoreAddressDTO.entityToDTO(s.getStoreAddress()))
                .businessLicense(String.valueOf(s.getBusinessLicense()))
                .userId(s.getUserAccount().getId())
                .storeImageUrl(s.getStoreImageUrl())
                .menus(s.getMenus().stream().map(MenuDTO::entityToDTO).collect(Collectors.toList()))
                .build();
    }
}

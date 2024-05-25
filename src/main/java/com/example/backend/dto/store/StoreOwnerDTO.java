package com.example.backend.dto.store;

import com.example.backend.dto.menu.MenuDTO;
import com.example.backend.entity.store.Category;
import com.example.backend.entity.store.Store;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreOwnerDTO {
    @JsonIgnore // ID 필드를 JSON 응답에서 숨깁니다.
    private Long id;
    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액
    private String storeImageUrl; // 배경 이미지
    private Category category;
    private String info;
    private StoreAddressDTO storeAddress; // 거리 정보
    private String businessLicense; // 사업자 등록증
    @JsonIgnore // UserAccount의 ID 필드를 JSON 응답에서 숨깁니다.
    private Long userId;
    private LocalTime openTime; // 오픈 시간
    private LocalTime closeTime; // 마감 시간
    @JsonIgnore // 메뉴 목록을 JSON 응답에서 숨깁니다.
    @Builder.Default
    private List<MenuDTO> menus = new ArrayList<>();

    public static StoreOwnerDTO entityToDTO(Store s) {
        return StoreOwnerDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .minOrderPrice(s.getMinOrderPrice())
                .category(s.getCategory())
                .info(s.getInfo())
                .storeAddress(StoreAddressDTO.entityToDTO(s.getStoreAddress()))
                .businessLicense(String.valueOf(s.getBusinessLicense()))
                .userId(s.getUserAccount().getId())
                .storeImageUrl(s.getStoreImageUrl())
                .openTime(s.getOpenTime())
                .closeTime(s.getCloseTime())
                .menus(s.getMenus().stream().map(MenuDTO::entityToDTO).collect(Collectors.toList()))
                .build();
    }
}

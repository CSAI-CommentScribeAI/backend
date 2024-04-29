package com.example.backend.entity.store;

import com.example.backend.entity.TimeZone;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name="store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store extends TimeZone {

    @Id
    @GeneratedValue
    @Column(name = "store_id")
    private Long id;

    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액

    @Lob
    private String storeImageUrl; // 배경 이미지
    @Enumerated(EnumType.STRING)
    private Category category; // 매장 카테고리

    @Lob
    private String info; // 가게 정보

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

    @Column(length = 10)
    private String businessLicense; // 사업자 등록증

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래 키 제약 조건 추가
    private UserAccount userAccount;

    @OneToMany(mappedBy = "store") // mappedBy 속성 수정
    @Builder.Default
    @JsonIgnore
    private List<Menu> menus = new ArrayList<>();

    private String grade; // 매장 평점

    public String getBusinessLicense() {
        return this.businessLicense;
    }
    // 메뉴 삭제
    public void removeMenu() {
        for (Menu m : menus) {
            m.removeStore();
        }
        menus = null;
    }

    // setUserAddress 메서드 추가
    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    // 메뉴를 추가할 때 양방향 관계 설정
    public void addMenu(Menu menu) {
        menu.setStore(this); // 메뉴에 매장 설정
        this.menus.add(menu); // 매장에 메뉴 추가
    }
    public void deleteMenu(Menu menu) {
        if (this.menus.contains(menu)) {
            this.menus.remove(menu);
        }
    }



    // 사용자의 역할을 확인하여 매장 등록 권한을 부여하는 메서드
    public void registerStore(UserAccount userAccount) {
        // 사용자의 역할이 "user_owner"인 경우에만 매장을 등록할 수 있도록 함
        if (userAccount != null && "user_owner".equals(userAccount.getUserRole())) {
            // 매장 등록 로직
            System.out.println("매장을 등록합니다.");
        } else {
            try {
                throw new IllegalAccessException("매장을 등록할 수 있는 권한이 없습니다.");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

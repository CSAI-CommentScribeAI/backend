package com.example.backend.entity.store;

import com.example.backend.entity.TimeZone;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.userAccount.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Store")
public class Store extends TimeZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 매장명
    private int minOrderPrice; // 최소 주문금액

    @Lob
    private String storeImageUrl; // 배경 이미지
    @Enumerated(EnumType.STRING)
    private Category category; // 매장 카테고리


    @Lob
    private String info; // 가게 정보

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Store와의 일대일 매핑
    @JoinColumn(name = "store_address_id", referencedColumnName = "id") // StoreAddress 테이블의 id를 참조합니다.
    private StoreAddress storeAddress; // store 거리 정보

    @Getter
    @Column // 고유값으로 설정
    private String businessLicense; // 사업자 등록증

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") // 외래 키 제약 조건 추가
    private UserAccount userAccount;

    @OneToMany(mappedBy = "store") // mappedBy 속성 수정
    @Builder.Default
    @JsonIgnore
    private List<Menu> menus = new ArrayList<>();

    private String grade; // 매장 평점

    @Column(name = "recent_order_id")
    private Long recentlyOrderId;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

}

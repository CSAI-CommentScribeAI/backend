package com.example.backend.UserAccount.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Entity
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter
    @Column(nullable = false)
    private String full_address; // 전체 주소

    @Setter
    @Column(nullable = false)
    private String road_address; // 도로명 주소

    @Setter
    private String jibun_address; // 지번 주소

    @Setter
    private String postal_code; // 우편번호

    @Setter
    private Double latitude; // 위도

    @Setter
    private Double longitude; // 경도

    @Builder
    public UserAddress(Long id, UserAccount userAccount, String full_address, String road_address, String jibun_address, String postal_code, Double latitude, Double longitude) {
        this.id = id;
        this.userAccount = userAccount;
        this.full_address = full_address;
        this.road_address = road_address;
        this.jibun_address = jibun_address;
        this.postal_code = postal_code;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public UserAddress() {
    }
}

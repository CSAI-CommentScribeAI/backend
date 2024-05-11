package com.example.backend.entity.userAccount;

import javax.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @Column(nullable = false)
    private String fullAddress; // 전체 주소

    @Column(nullable = false)
    private String roadAddress; // 도로명 주소

    private String jibunAddress; // 지번 주소

    @Column(nullable = false)
    private String postalCode; // 우편번호

    private Double latitude; // 위도

    private Double longitude; // 경도
}

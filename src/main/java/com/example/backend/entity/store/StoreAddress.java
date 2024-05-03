package com.example.backend.entity.store;

import com.example.backend.entity.TimeZone;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreAddress extends TimeZone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", unique = true)
    private Store store;

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

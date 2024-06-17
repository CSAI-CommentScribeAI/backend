package com.example.backend.entity.menu;

import com.example.backend.entity.TimeZone;
import com.example.backend.entity.store.Store;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString(exclude = "store")
public class Menu extends TimeZone {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id") // 외래 키 설정
    private Store store;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private int price;
    private String menuDetail;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

}

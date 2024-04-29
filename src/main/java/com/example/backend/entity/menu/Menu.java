package com.example.backend.entity.menu;

import com.example.backend.entity.TimeZone;
import com.example.backend.entity.store.Store;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "store")
public class Menu extends TimeZone {

    @Id @GeneratedValue
    @Column(name = "menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id") // 외래 키 설정
    private Store store;

    private String name;
    private String imageUrl;
    private int price;
    private String menuDetail;
    @Builder.Default
    private boolean visible = true;

    public void setStore(Store store) {
        this.store = store;
        if (!store.getMenus().contains(this)) {
            store.getMenus().add(this);
        }
    }

    // Store와의 관계 해제 메서드
    public void removeStore() {
        if (store != null) {
            store.getMenus().remove(this);
            store = null;
        }
    }
}

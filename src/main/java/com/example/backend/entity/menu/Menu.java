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
    @Column(columnDefinition = "TEXT")
    private String imageUrl;
    private int price;
    private String menuDetail;
    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    public void setStore(Store store) {
        this.store = store;
        if (!store.getMenus().contains(this)) {
            store.getMenus().add(this);
        }
    }

    public void removeStore() {
        if (store != null) {
            store.getMenus().remove(this);
            store = null;
        }
    }
}

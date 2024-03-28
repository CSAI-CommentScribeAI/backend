package com.example.backend.shop.entity;

import com.example.backend.food.entity.Food;
import com.example.backend.food.entity.Group;
import com.example.backend.root.AuditableEntity;
import com.example.backend.UserAccount.entity.UserAccount;
import lombok.*;
import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "shop")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop extends AuditableEntity {
    @Column(name = "shop_name", nullable = false, length = 50)
    private String name;

    @Column(name = "phone_num", nullable = false, length = 20)
    private String phoneNum;

    @Column(name = "short_description", nullable = false, length = 100)
    private String shortDescription;

    @Column(name = "long_description", nullable = false, length = 1024)
    private String longDescription;

    @Column(name = "supported_order_method", nullable = false)
    private ShopSupportedOrderType supportedOrderType;

    @Column(name = "supported_payment", nullable = false)
    private ShopSupportedPayment supportedPayment;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;

    @Column(name = "delivery_fee", nullable = false)
    private int deliveryFee;

    @Column(name = "min_order_price", nullable = false)
    private int minOrderPrice;

    @Column(name = "shop_status", nullable = false)
    private ShopStatus shopStatus;

    @Column(name = "register_number", nullable = false, unique = true, length = 30)
    private String registerNumber;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserAccount userAccount;

    @Column(name = "doro_address", nullable = false)
    private String doroAddress;

    @Column(name = "doro_index", nullable = false)
    private int doroIndex;

    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<DeliverySupportedRegions> regions = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<Food> foods = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.PERSIST)
    private final List<Group> groups = new ArrayList<>();

    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL)
    private final List<ShopCategory> shopCategories = new ArrayList<>();

    public void changeName(@NonNull String name) {
        if (name.isBlank()) return;
        this.name = name;
    }
}
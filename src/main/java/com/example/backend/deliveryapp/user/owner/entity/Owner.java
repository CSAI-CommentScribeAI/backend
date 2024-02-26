package com.example.backend.deliveryapp.user.owner.entity;

import com.example.backend.deliveryapp.shop.entity.Shop;
import com.example.backend.deliveryapp.user.UserAccount;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Owner extends UserAccount {
    @OneToMany(mappedBy = "owner")
    private final List<Shop> shops = new ArrayList<>();

    public void addShop(Shop shop) {
        this.shops.add(shop);
    }

    @Builder
    public Owner(String username, String email, String phoneNum) {
        super(username, email, phoneNum);
    }
}

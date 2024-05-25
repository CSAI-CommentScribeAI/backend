package com.example.backend.entity.cart;

import com.example.backend.entity.userAccount.UserAccount;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserAccount user;

    private Long storeId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();  // 리스트 초기화

    public List<CartItem> getItems() {
        return cartItems;
    }

    public void clearItems() {
        this.cartItems.clear();
    }
}

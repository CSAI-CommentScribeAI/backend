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

    private String cartStatus;
    private int totalPrice;
    private Long storeId;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();  // 리스트 초기화

    public void calculateTotalPriceAndStoreId() {
        int totalPrice = 0;
        Long storeId = null;
        for (CartItem cartItem : cartItems) {
            if (cartItem.getMenu() != null) {
                totalPrice += cartItem.getMenu().getPrice() * cartItem.getQuantity();
                if (storeId == null) {
                    storeId = cartItem.getMenu().getStore().getId();
                }
            }
        }
        this.totalPrice = totalPrice;
        this.storeId = storeId;
    }

    public List<CartItem> getItems() {
        return cartItems;  // 수정된 부분
    }

    public void clearItems() {
        this.cartItems.clear();  // 수정된 부분
    }
}

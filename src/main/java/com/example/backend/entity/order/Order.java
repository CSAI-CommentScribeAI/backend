package com.example.backend.entity.order;

import com.example.backend.entity.TimeZone;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ORDERS")
public class Order extends TimeZone {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;

    private Long storeId;

    @Column(nullable = false)
    private int totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderMenu> orderMenus;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public int getTotalAmount() {
        return totalPrice;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalPrice = totalPrice;
    }


    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public void setStore(Long storeId) {
        this.storeId = storeId;
    }
}

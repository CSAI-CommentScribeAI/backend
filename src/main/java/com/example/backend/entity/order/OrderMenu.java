package com.example.backend.entity.order;

import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.userAccount.UserAccount;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_menu")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Better specify strategy for clarity
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false) // Assuming an order must exist
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false) // Assuming a menu must exist
    private Menu menu;

    private String imageUrl;
    @Column(nullable = false)
    private int quantity;

}

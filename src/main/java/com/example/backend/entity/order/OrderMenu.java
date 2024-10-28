package com.example.backend.entity.order;

import com.example.backend.entity.menu.Menu;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Order_Menu")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Better specify strategy for clarity
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false) // Assuming an order must exist
    private UserOrder userOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false) // Assuming a menu must exist
    private Menu menu;

    private String menuName;
    private String imageUrl;
    @Column(nullable = false)
    private int quantity;

}

package com.example.backend.entity.cart;

import com.example.backend.entity.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name ="cartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 ID 생성
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id") // 외래키 설정
    private Menu menu; // Menu 객체 참조

    @ManyToOne
    @JoinColumn(name = "cart_id") // Cart 참조를 위한 외래키 설정
    private Cart cart;

    private String imageUrl;

}

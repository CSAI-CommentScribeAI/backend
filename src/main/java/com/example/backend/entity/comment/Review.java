package com.example.backend.entity.comment;

import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import java.security.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // 리뷰의 고유 ID

    @Column(nullable = false)
    private String comment;  // 리뷰 내용

    @Column(nullable = false)
    private LocalDateTime createAt;  // 리뷰 작성 시간

    @Column(nullable = true)
    private LocalDateTime updateAt;  // 리뷰 수정 시간

    @Column(nullable = true)
    private LocalDateTime deleteAt;  // 리뷰 삭제 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;  // User 엔티티와의 관계 - 비식별 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;  // Store 엔티티와의 관계 - 비식별 관계

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    private Order order;  // Order 엔티티와의 관계 - 식별 관계, Order의 id를 참조

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL)
    private Reply reply;  // Review와 Reply의 일대일 관계
}

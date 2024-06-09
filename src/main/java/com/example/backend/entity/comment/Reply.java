package com.example.backend.entity.comment;

import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import java.security.Timestamp;
import java.time.LocalDateTime;
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
import lombok.Data;

@Entity
@Data
@Table(name = "reply")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 리뷰의 고유 ID

    @Column(columnDefinition = "TEXT")
    private String comment;  // 리뷰 내용

    @Column(nullable = false)
    private LocalDateTime createAt;  // 리뷰 작성 시간

    private LocalDateTime updateAt;  // 리뷰 수정 시간

    private LocalDateTime deleteAt;  // 리뷰 삭제 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;  // User 엔티티와의 관계 - 비식별 관계

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;  // Review 엔티티와의 관계 - 식별 관계, Review의 id를 참조
}

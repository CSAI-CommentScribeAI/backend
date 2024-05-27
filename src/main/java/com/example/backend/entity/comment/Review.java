package com.example.backend.entity.comment;

import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @Column(name = "order_id")
    private Long id;

    @OneToOne
    @MapsId // Review의 id는 Order의 id와 동일합니다.
    @JoinColumn(name = "order_id")
    private Order order;

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

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Reply> replies = new ArrayList<>();  // Review와 Reply의 일대다 관계

    public List<Reply> getReplies() {
        return replies == null ? new ArrayList<>() : replies;
    }
}

package com.example.backend.entity.openAI;

import com.example.backend.entity.comment.Review;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
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
@Table(name = "reply_save")
public class ReplySave {

    @Id
    @Column(name = "review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @Column(columnDefinition = "TEXT")
    private String replyContent;
    private float sentimentScore;
    private String sentimentLabel;
    private LocalDateTime createdAt;
}

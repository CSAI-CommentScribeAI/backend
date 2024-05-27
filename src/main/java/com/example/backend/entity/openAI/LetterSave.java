package com.example.backend.entity.openAI;

import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "letter_save")
public class LetterSave {


    @Id
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @Column(name = "message_content", length = 2000)  // length 속성을 사용하여 길이를 늘림
    private String messageContent;
    private float sentimentScore;
    private String sentimentLabel;
    private LocalDateTime createdAt;
}

package com.example.backend.entity.userAccount;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

import com.example.backend.entity.store.Store;
import lombok.*;@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(indexes = {
        @Index(name = "idx_user_id", columnList = "userId"),
        @Index(name = "idx_created_at", columnList = "createdAt"),
        @Index(name = "idx_created_by", columnList = "createdBy")
})
public class UserAccount extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String userId;

    @Column
    private String password;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(length = 100)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL)
    @JoinColumn(name = "user_address_id")
    private UserAddress userAddress;

    @Column(name = "store_id")
    private Long storeId; // 매장 ID

    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Store> stores = new LinkedHashSet<>();

    @Builder
    public UserAccount(Long id, String userId, String password, String email, String nickname, UserRole userRole, UserAddress userAddress, Long storeId) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.userRole = userRole;
        this.userAddress = userAddress;
        this.storeId = storeId; // 매장 ID 설정
        this.stores = stores != null ? stores : new LinkedHashSet<>();
    }
    public void setUserAddress(int intExact) {
        this.userAddress=userAddress;
    }

    public void setStore(Store store) {
        this.storeId = store.getId();
    }
}

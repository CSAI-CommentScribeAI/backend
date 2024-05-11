package com.example.backend.entity.userAccount;

import com.example.backend.entity.cart.Cart;
import com.example.backend.entity.store.Store;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter // 일관성을 위해 클래스 레벨에 Setter 적용
@ToString
@NoArgsConstructor // Lombok을 사용하여 기본 생성자 생성
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

    private int address;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAddress> userAddresses = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Store> stores = new LinkedHashSet<>();

    @Builder
    public UserAccount(Long id, String userId, String password, String email, String nickname, int address, UserRole userRole, Set<UserAddress> userAddresses, Set<Store> stores) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.address = address;
        this.userRole = userRole;
        this.userAddresses = userAddresses != null ? userAddresses : new LinkedHashSet<>();
        this.stores = stores != null ? stores : new LinkedHashSet<>();
    }

    public void setStore(Store store) {
        this.stores = stores;
    }

}

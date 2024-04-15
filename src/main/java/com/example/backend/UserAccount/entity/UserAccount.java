package com.example.backend.UserAccount.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.*;

import com.example.backend.shop.entity.Shop;
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

    @Column(nullable = false)
    private String password;

    @Column(length = 100)
    private String userName;

    @Column(length = 100)
    private String phone;

    @Column(length = 100)
    private String nickname;

    private String memo;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserAddress> userAddresses = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Shop> shops = new LinkedHashSet<>();

    @Builder
    public UserAccount(Long id, String userId, String password, String userName, String phone, String nickname, String memo, UserRole userRole, Set<UserAddress> userAddresses, Set<Shop> shops) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.nickname = nickname;
        this.memo = memo;
        this.userRole = userRole;
        this.userAddresses = userAddresses != null ? userAddresses : new LinkedHashSet<>();
        this.shops = shops != null ? shops : new LinkedHashSet<>();
    }
}

package com.example.backend.UserAccount.entity;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
@Table(indexes = {
        @Index(columnList = "userId"),
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class UserAccount extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String userId;
    @Setter
    @Column(nullable = false)
    private String password;

    @Setter
    @Column(length = 100)
    private String email;
    @Setter
    @Column(length = 100)
    private String userName;
    @Setter
    @Column(length = 100)
    private String phone;
    @Setter
    @Column(length = 100)
    private String nickname;
    @Setter
    private String memo;
    @Setter
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @ToString.Exclude
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private final Set<UserAddress> userAddresses = new LinkedHashSet<>();


    protected UserAccount() {
    }

    @Builder
    public UserAccount(Long id, String userId, String password, String email, String userName, String phone, String nickname, String memo, UserRole userRole) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.userName = userName;
        this.phone = phone;
        this.nickname = nickname;
        this.memo = memo;
        this.userRole = userRole;
    }

}

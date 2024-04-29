package com.example.backend.entity.userAccount;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column( updatable = false)
    private LocalDateTime createdAt; // 생성일시

    @Setter
    @CreatedBy
    @Column( updatable = false, length = 100)
    private String createdBy; // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column()
    private LocalDateTime modifiedAt; // 수정일시

    @Setter
    @LastModifiedBy
    @Column( length = 100)
    private String modifiedBy; // 수정자

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}
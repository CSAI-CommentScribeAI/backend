package com.example.backend.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class TimeZone {
    @Column(updatable = false)
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @PrePersist
    public void createTime(){
        LocalDateTime now = LocalDateTime.now().withNano(0);
        this.createdTime = now;
        this.updatedTime = now;
    }

    @PreUpdate
    public void updateTime(){
        this.updatedTime = LocalDateTime.now().withNano(0);
    }
}

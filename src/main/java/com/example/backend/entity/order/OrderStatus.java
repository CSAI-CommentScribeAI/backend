package com.example.backend.entity.order;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {
    PAYMENT,
    REQUEST,     // 대기 중
    ACCEPT,      // 수락
    DELIVERED,   // 배달 완료
    CANCEL;      // 취소

    @JsonCreator
    public static OrderStatus from(String s){
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
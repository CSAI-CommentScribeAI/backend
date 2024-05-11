package com.example.backend.entity.order;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum OrderStatus {

    CART,
    WAITING,     // 대기 중
    ACCEPT,      // 수락
    IN_PROGRESS, // 조리 중 (수락 버튼을 누르면 변경됨)
    DISPATCHED,  // 배차 완료
    DELIVERED,   // 배달 완료
    CANCEL;      // 취소

    @JsonCreator
    public static OrderStatus from(String s){
        return OrderStatus.valueOf(s.toUpperCase());
    }
}
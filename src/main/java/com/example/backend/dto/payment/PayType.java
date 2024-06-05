package com.example.backend.dto.payment;

public enum PayType {
    CARD("카드"),
    CASH("현금");

    private String description;

    PayType(String description) {
        this.description = description;
    }

    //각 결제 유형에 대한설명
    public String getDescription() {
        return description;
    }
}
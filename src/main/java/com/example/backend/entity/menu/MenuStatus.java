package com.example.backend.entity.menu;

public enum MenuStatus {
    SOLD("품절"),
    HIDING("숨김"),
    SALE("판매중");

    private final String description;

    MenuStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

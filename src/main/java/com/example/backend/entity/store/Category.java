package com.example.backend.entity.store;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Category {
    HAMBURGER("햄버거"),
    PIZZA("피자"),
    COFFEE("커피"),
    DESSERT("디저트"),
    KOREANFOOD("한식"),
    CHINESEFOOD("중식"),
    FLOURBASEDFOOD("분식"),
    JAPANESEFOOD("일식"),
    CHICKEN("치킨");

    @JsonCreator
    public static Category from(String s) {
        return Category.valueOf(s.toUpperCase());
    }

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
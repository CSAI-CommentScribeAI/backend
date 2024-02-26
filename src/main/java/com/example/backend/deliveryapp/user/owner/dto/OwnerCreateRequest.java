package com.example.backend.deliveryapp.user.owner.dto;

public class OwnerCreateRequest {
    private String userName;
    private String email;
    private String phoneNum;

    // 기본 생성자 추가
    public OwnerCreateRequest() {
    }

    public OwnerCreateRequest(String username, String email, String phoneNum) {
        this.userName = username;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneNum() {
        return this.phoneNum;
    }
}

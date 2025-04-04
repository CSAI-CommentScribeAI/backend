package com.example.backend.exception;

public class Message {
    public static final String ILLEGAL_ARGUMENT = "잘못된 정보를 넣었습니다.";

    // user
    public static final String USER_NOT_FOUND = "등록되지 않은 회원입니다.";
    public static final String USER_DUPLICATED = "중복된 회원입니다.";
    public static final String USER_UNAUTHORIZED = "접근권한이 없는 회원입니다.";
    public static final String USER_TOKEN_EXPIRED = "해당유저의 토큰이 만료되었습니다.";
    public static final String USER_TOKEN_NOT_FOUND = "토큰 정보를 찾을 수 없습니다.";
    public static final String USER_INFORMATION_EMPTY = "유저 필수정보 중 비어있는 칸이 존재합니다.";

    // jwt
    public static final String JWT_TOKEN_EXPIRED = "JWT 토큰이 만료되었습니다.";
    public static final String JWT_UNSUPPORTED = "지원하지 않는 JWT 토큰입니다.";
    public static final String JWT_MALFORMED = "올바른 JWT 토큰의 형태가 아닙니다.";
    public static final String JWT_SIGNATURE = "올바른 SIGNATURE가 아닙니다.";
    public static final String JWT_ILLEGAL_ARGUMENT = "잘못된 정보를 넣었습니다.";


    // store
    public static final String STORE_NOT_FOUND = "가게를 찾지 못했습니다.";
    public static final String STORE_DISTANCE = "올바른 값을 입력해주세요. ex) 0.9km";
    public static final String ALREADY_STORE_BOSS_ASSIGN = "해당 사장님으로 등록된 가게가 존재합니다.";

    // order
    public static final String ORDER_NOT_FOUND = "주문을 찾지 못했습니다.";
    public static final String ORDER_ILLEGAL = "주문 금액이 올바르지 않습니다.";
    public static final String ORDER_STATUS = "처리할 수 없는 주문 상태입니다.";
    public static final String ORDER_TIMEINDEX_ILLEGAL = "Time Index가 올바르지 않습니다. (오늘점심: 0, 오늘저녁: 1, 내일점심: 2, 내일저녁: 3)";

    // userOrder
    public static final String USER_ORDER_DETAIL_NOT_FOUND = "유저 주문을 찾을 수 없습니다.";

    // menu
    public static final String MENU_NOT_FOUND = "메뉴를 찾지 못했습니다.";
    public static final String MENU_NOT_MATCHING = "해당 지점에 있는 메뉴가 아닙니다.";

    // destination
    public static final String DESTINATION_NOT_FOUND = "목적지를 찾지 못했습니다.";

    // awsS3
    public static final String FILE_UPLOAD_FAILED = "파일 업로드에 실패했습니다.";
    public static final String EMPTY_FILE = "파일을 찾을 수 없습니다.";

    // payment
    public static final String PAYMENT_NOT_FOUND = "PAYMENT를 찾지 못했습니다.";
    public static final String PAYMENT_AMOUNT_NOT_MATCH = "결제금액이 올바르지 않습니다.";
    public static final String PAYMENT_STATUS = "결제를 완료했는지 확인해주세요.";
    public static final String PAYMENT_AMOUNT_EXP = "결제 금액을 확인해주세요";
    public static final String ALREADY_PAMENT = "이미 결제가 완료되었습니다 확인해주세요";
    // menuOption
    public static final String MENU_OPTION_NOT_FOUND = "옵션 값을 찾을 수 없습니다.";

    // review
    public static final String REVIEW_NOT_FOUND = "리뷰를 찾지 못했습니다.";
    public static final String REVIEW_ALREADY_EXISTS = "리뷰가 이미 존재합니다.";

    public static final String CART_NOT_FOUND =  "사용자의 장바구니 정보를 찾을 수 없습니다.";

}


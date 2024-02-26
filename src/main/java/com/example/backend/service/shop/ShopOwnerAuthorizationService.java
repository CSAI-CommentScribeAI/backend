package com.example.backend.service.shop;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ShopOwnerAuthorizationService {

    public boolean isShopOwner(Authentication authentication, Long shopId) {
        // 현재 인증된 사용자의 ID와 매장 소유자의 ID를 비교하여 확인하는 로직을 작성합니다.
        // 여기서는 간단하게 사용자 ID가 매장 소유자의 ID와 일치하는지 확인하는 예시를 보여드리겠습니다.
        String currentUserId = authentication.getName();
        return currentUserId.equals(shopId.toString());
    }
}

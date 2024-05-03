package com.example.backend.service.menu;

import com.example.backend.dto.menuOption.MenuOptionTitleDTO;
import com.example.backend.entity.menu.MenuOptionTitle;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.menu.MenuOptionTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuOptionTitleService {
    private final MenuOptionTitleRepository menuOptionTitleRepository;
    private final UserAccountRepository userAccountRepository;

    public MenuOptionTitleDTO insertMenuOptionTitle(Authentication authentication,MenuOptionTitleDTO menuOptionTitleDTO) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("가게를 등록할 권한이 없습니다.");
        }

        MenuOptionTitle menuOptionTitle = MenuOptionTitle.builder()
                .titleName(menuOptionTitleDTO.getTitleName())
                .multipleCheck(menuOptionTitleDTO.isMultipleCheck())
                .build();
        menuOptionTitle = menuOptionTitleRepository.save(menuOptionTitle);
        return new MenuOptionTitleDTO(menuOptionTitle.getId(), menuOptionTitle.getTitleName(), menuOptionTitle.isMultipleCheck());
    }

    public MenuOptionTitleDTO getMenuOptionTitleById(Long id) {
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID에 해당하는 메뉴 옵션 제목을 찾을 수 없습니다: " + id));
        return new MenuOptionTitleDTO(menuOptionTitle.getId(), menuOptionTitle.getTitleName(), menuOptionTitle.isMultipleCheck());
    }

    public MenuOptionTitleDTO updateMenuOptionTitle(Authentication authentication,
                                                    Long id, MenuOptionTitleDTO menuOptionTitleDTO) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("가게를 등록할 권한이 없습니다.");
        }
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID에 해당하는 메뉴 옵션 제목을 찾을 수 없습니다: " + id));
        menuOptionTitle.setTitleName(menuOptionTitleDTO.getTitleName());
        menuOptionTitle.setMultipleCheck(menuOptionTitleDTO.isMultipleCheck());
        menuOptionTitle = menuOptionTitleRepository.save(menuOptionTitle);
        return new MenuOptionTitleDTO(menuOptionTitle.getId(), menuOptionTitle.getTitleName(), menuOptionTitle.isMultipleCheck());
    }

    public void deleteMenuOptionTitle(Authentication authentication,Long id) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("가게를 등록할 권한이 없습니다.");
        }
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID에 해당하는 메뉴 옵션 제목을 찾을 수 없습니다: " + id));
        menuOptionTitleRepository.delete(menuOptionTitle);
    }
}

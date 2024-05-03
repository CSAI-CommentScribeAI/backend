package com.example.backend.service.menu;

import com.example.backend.dto.menuOption.MenuOptionDTO;
import com.example.backend.entity.menu.MenuOptionTitle;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.menu.MenuOptionTitleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.example.backend.repository.menu.MenuOptionRepository;
import com.example.backend.entity.menu.MenuOption;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuOptionService {
    private final MenuOptionRepository menuOptionRepository;
    private final MenuOptionTitleRepository menuOptionTitleRepository;
    private final UserAccountRepository userAccountRepository;
    public List<MenuOption> findMenuOptionsByMenuId(Long menuId) {
        return menuOptionRepository.findAllByMenuId(menuId);
    }
    public MenuOptionDTO insertMenuOption(Authentication authentication,MenuOptionDTO menuOptionDTO) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("User does not have permission to register a store.");
        }
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(menuOptionDTO.getMenuOptionTitleId())
                .orElseThrow(() -> new RuntimeException("MenuOptionTitle not found"));

        MenuOption menuOption = MenuOption.builder()
                .optionName(menuOptionDTO.getOptionName())
                .optionPrice(menuOptionDTO.getOptionPrice())
                .menuOptionTitle(menuOptionTitle) // Set menuOptionTitle
                .build();

        menuOption = menuOptionRepository.save(menuOption);
        return new MenuOptionDTO(menuOption.getId(), menuOption.getOptionName(), menuOption.getOptionPrice(), menuOption.getMenuOptionTitle().getId());
    }

    public MenuOptionDTO updateMenuOption(Authentication authentication,Long id, MenuOptionDTO menuOptionDTO) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("User does not have permission to register a store.");
        }
        MenuOption menuOption = menuOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MenuOption not found"));
        menuOption.setOptionName(menuOptionDTO.getOptionName());
        menuOption.setOptionPrice(menuOptionDTO.getOptionPrice());
        menuOption = menuOptionRepository.save(menuOption);
        return MenuOptionDTO.builder()
                .menuOption_id(menuOption.getId())
                .optionName(menuOption.getOptionName())
                .optionPrice(menuOption.getOptionPrice())
                .build();
    }

    public void deleteMenuOption(Authentication authentication,Long id) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("User does not have permission to register a store.");
        }
        menuOptionRepository.deleteById(id);
    }
}


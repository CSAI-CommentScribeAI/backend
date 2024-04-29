package com.example.backend.service.menu;

import com.example.backend.dto.menuOption.MenuOptionDTO;
import com.example.backend.entity.menu.MenuOptionTitle;
import com.example.backend.repository.menu.MenuOptionTitleRepository;
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
    public List<MenuOption> findMenuOptionsByMenuId(Long menuId) {
        return menuOptionRepository.findAllByMenuId(menuId);
    }
    public MenuOptionDTO insertMenuOption(MenuOptionDTO menuOptionDTO) {
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

    public MenuOptionDTO updateMenuOption(Long id, MenuOptionDTO menuOptionDTO) {
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

    public void deleteMenuOption(Long id) {
        menuOptionRepository.deleteById(id);
    }
}


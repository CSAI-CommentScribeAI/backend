package com.example.backend.service.menu;

import com.example.backend.dto.menuOption.MenuOptionTitleDTO;
import com.example.backend.entity.menu.MenuOptionTitle;
import com.example.backend.repository.menu.MenuOptionTitleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuOptionTitleService {
    private final MenuOptionTitleRepository menuOptionTitleRepository;

    public MenuOptionTitleDTO insertMenuOptionTitle(MenuOptionTitleDTO menuOptionTitleDTO) {
        MenuOptionTitle menuOptionTitle = MenuOptionTitle.builder()
                .titleName(menuOptionTitleDTO.getTitleName())
                .multipleCheck(menuOptionTitleDTO.isMultipleCheck())
                .build();
        menuOptionTitle = menuOptionTitleRepository.save(menuOptionTitle);
        return new MenuOptionTitleDTO(menuOptionTitle.getId(), menuOptionTitle.getTitleName(), menuOptionTitle.isMultipleCheck());
    }

    public MenuOptionTitleDTO getMenuOptionTitleById(Long id) {
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu option title not found with ID: " + id));
        return new MenuOptionTitleDTO(menuOptionTitle.getId(), menuOptionTitle.getTitleName(), menuOptionTitle.isMultipleCheck());
    }

    public MenuOptionTitleDTO updateMenuOptionTitle(Long id, MenuOptionTitleDTO menuOptionTitleDTO) {
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu option title not found with ID: " + id));
        menuOptionTitle.setTitleName(menuOptionTitleDTO.getTitleName());
        menuOptionTitle.setMultipleCheck(menuOptionTitleDTO.isMultipleCheck());
        menuOptionTitle = menuOptionTitleRepository.save(menuOptionTitle);
        return new MenuOptionTitleDTO(menuOptionTitle.getId(), menuOptionTitle.getTitleName(), menuOptionTitle.isMultipleCheck());
    }

    public void deleteMenuOptionTitle(Long id) {
        MenuOptionTitle menuOptionTitle = menuOptionTitleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu option title not found with ID: " + id));
        menuOptionTitleRepository.delete(menuOptionTitle);
    }
}

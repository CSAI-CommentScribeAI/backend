package com.example.backend.repository.menu;

import com.example.backend.entity.menu.MenuOption;
import com.example.backend.entity.menu.MenuOptionTitle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuOptionRepository extends JpaRepository<MenuOption, Long> {

    public List<MenuOption> findAllByMenuOptionTitle(MenuOptionTitle menuOptionTitle);

    List<MenuOption> findAllByMenuId(Long menuId);
}

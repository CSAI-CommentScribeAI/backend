package com.example.backend.repository.menu;

import com.example.backend.entity.menu.MenuOptionTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuOptionTitleRepository extends JpaRepository<MenuOptionTitle, Long> {

}

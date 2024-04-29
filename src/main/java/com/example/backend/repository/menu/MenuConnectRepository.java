package com.example.backend.repository.menu;

import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.menu.MenuConnect;
import com.example.backend.entity.menu.MenuOptionTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuConnectRepository extends JpaRepository<MenuConnect, Integer> {

    @Query("select mc.menuOptionTitle from MenuConnect mc where mc.menu = :menu")
    List<MenuOptionTitle> findByMenu(@Param("menu") Menu menu);
}

package com.example.backend.service.menu;

import com.example.backend.dto.menu.MenuRequestDTO;
import com.example.backend.dto.menu.MenuResponseDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MenuService {
    MenuResponseDTO addMenu(Authentication authentication, MenuRequestDTO menuDTO, MultipartFile multipartFile);

    MenuResponseDTO updateMenu(Authentication authentication, Long menuId, MenuRequestDTO menuDTO, MultipartFile multipartFile);

    String deleteMenu(Authentication authentication, Long menuId);

    List<MenuResponseDTO> findMenusByStoreId(Long storeId);

    List<MenuResponseDTO> createMenuList(List<MenuRequestDTO> menuRequestDTOs);
}

package com.example.backend.controller.menu;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.menu.*;
import com.example.backend.service.menu.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 스토어 ID에 따라 해당 스토어의 메뉴를 조회
    @GetMapping("/{storeId}/menus")
    public ResponseDTO<?> getMenusByStoreId(@PathVariable Long storeId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.findMenusByStoreId(storeId));
    }

    // 스토어 ID와 메뉴 정보를 받아 메뉴를 추가
    @PostMapping("/{storeId}/menu")
    public ResponseDTO<MenuResponseDTO> addMenu(Authentication authentication,
                                                @ModelAttribute MenuRequestDTO menuDTO,
                                                @RequestParam("file") MultipartFile multipartFile) {
        MenuResponseDTO menu = menuService.addMenu(authentication, menuDTO, multipartFile);
        return new ResponseDTO<>(HttpStatus.CREATED.value(), menu);
    }

    // 스토어 ID와 메뉴 ID를 받아 메뉴 정보를 업데이트
    @PutMapping("/{storeId}/{menuId}")
    public ResponseDTO<MenuResponseDTO> updateMenu(Authentication authentication,
                                                   @PathVariable Long menuId,
                                                   @ModelAttribute MenuRequestDTO menuDTO,
                                                   @RequestParam("file") MultipartFile multipartFile) {
        MenuResponseDTO menu = menuService.updateMenu(authentication, menuId, menuDTO, multipartFile);
        return new ResponseDTO<>(HttpStatus.OK.value(), menu);
    }
    // 스토어 ID와 메뉴 ID를 받아 메뉴를 삭제
    @DeleteMapping("/{storeId}/{menuId}")
    public ResponseDTO<String> deleteMenu(Authentication authentication,
                                          @PathVariable Long menuId) {
        String message = menuService.deleteMenu(authentication, menuId);
        return new ResponseDTO<>(HttpStatus.OK.value(), message);
    }

    @PostMapping("/menu_list")
    public ResponseDTO<List<MenuResponseDTO>> createMenuList(@RequestBody List<MenuRequestDTO> menuListDTO) {
        List<MenuResponseDTO> menus = menuService.createMenuList(menuListDTO);
        return new ResponseDTO<>(HttpStatus.CREATED.value(), menus);
    }
}

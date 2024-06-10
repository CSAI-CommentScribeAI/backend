package com.example.backend.controller.menu;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.menu.MenuInsertDTO;
import com.example.backend.dto.menu.MenuListDTO;
import com.example.backend.dto.menu.MenuUpdateDTO;
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
    public ResponseDTO<?> addMenu(Authentication authentication,
                                  @ModelAttribute MenuInsertDTO menuDTO,
                                  @RequestParam("file") MultipartFile multipartFile,
                                  @PathVariable Long storeId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.addMenu(authentication, menuDTO, multipartFile, storeId));
    }

    // 스토어 ID와 메뉴 ID를 받아 메뉴 정보를 업데이트
    @PutMapping("/{storeId}/{menuId}")
    public ResponseDTO<?> updateMenu(Authentication authentication,
                                     @PathVariable Long storeId,
                                     @PathVariable Long menuId,
                                     @ModelAttribute MenuUpdateDTO menuDTO,
                                     @RequestParam("file") MultipartFile multipartFile) {
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.updateMenu(authentication, storeId, menuId, menuDTO, multipartFile));
    }
    // 스토어 ID와 메뉴 ID를 받아 메뉴를 삭제
    @DeleteMapping("/{storeId}/{menuId}")
    public ResponseDTO<?> deleteMenu(Authentication authentication,
                                     @PathVariable Long storeId,
                                     @PathVariable Long menuId) {
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.deleteMenu(authentication, storeId, menuId));
    }

    @PostMapping("/menu_list")
    public ResponseDTO<?> createMenuList(@RequestBody List<MenuListDTO> menuListDTO) {
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.createMenuList(menuListDTO));
    }
}

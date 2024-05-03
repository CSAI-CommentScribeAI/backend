package com.example.backend.controller.menu;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.menu.MenuInsertDTO;
import com.example.backend.dto.menu.MenuUpdateDTO;
import com.example.backend.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping("/{menuId}")
    public ResponseDTO<?> selectMenu(@PathVariable Long menuId){
        return menuService.selectMenu(menuId);
    }

    @PostMapping("/")
    public ResponseDTO<?> addMenu(Authentication authentication,
                                     @ModelAttribute MenuInsertDTO menuDTO,
                                     @RequestPart("file") MultipartFile multipartFile){
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.addMenu(authentication, menuDTO, multipartFile));
    }

    @PutMapping("/{menuId}")
    public ResponseDTO<?> updateMenu(Authentication authentication,
                                     @PathVariable Long menuId,
                                     @RequestBody MenuUpdateDTO menuDTO){
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.updateMenu(authentication, menuId, menuDTO));
    }

    @DeleteMapping("/{menuId}")
    public ResponseDTO<?> deleteMenu(Authentication authentication,
                                     @PathVariable Long menuId){
        return new ResponseDTO<>(HttpStatus.OK.value(), menuService.deleteMenu(authentication, menuId));
    }
}

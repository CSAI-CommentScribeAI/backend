package com.example.backend.controller.menu;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.menuOption.MenuOptionTitleDTO;
import com.example.backend.service.menu.MenuOptionTitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/menuOptionTitle")
@RequiredArgsConstructor
public class MenuOptionTitleController {

    private final MenuOptionTitleService menuOptionTitleService;

    @PostMapping("/")
    public ResponseEntity<ResponseDTO> createMenuOptionTitle(Authentication authentication,
            @RequestBody MenuOptionTitleDTO menuOptionTitleDTO) {
        MenuOptionTitleDTO created = menuOptionTitleService.insertMenuOptionTitle(authentication,menuOptionTitleDTO);
        return new ResponseEntity<>(new ResponseDTO<>(HttpStatus.CREATED.value(), created), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getMenuOptionTitle(@PathVariable Long id) {
        MenuOptionTitleDTO menuOptionTitle = menuOptionTitleService.getMenuOptionTitleById(id);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), menuOptionTitle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO> updateMenuOptionTitle(Authentication authentication,@PathVariable Long id, @RequestBody MenuOptionTitleDTO menuOptionTitleDTO) {
        MenuOptionTitleDTO updated = menuOptionTitleService.updateMenuOptionTitle(authentication,id, menuOptionTitleDTO);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteMenuOptionTitle(Authentication authentication,@PathVariable Long id) {
        menuOptionTitleService.deleteMenuOptionTitle(authentication,id);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), "Menu option title deleted successfully"));
    }
}

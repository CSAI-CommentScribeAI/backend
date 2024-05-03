package com.example.backend.controller.menu;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.menuOption.MenuOptionDTO;
import com.example.backend.repository.menu.MenuOptionRepository;
import com.example.backend.service.menu.MenuOptionService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/menuOption")
@RequiredArgsConstructor
public class MenuOptionController {
    private final MenuOptionRepository menuOptionRepository;
    private final MenuOptionService menuOptionService;
    @GetMapping("/{menuId}")
    @ApiOperation(value = "메뉴에 맞는 옵션값을 반환한다.", notes = "menuId를 받아 옵션값을 리턴")
    public ResponseDTO<?> selectMenuOption(@PathVariable Long menuId){
        Map<String, Object> m = new HashMap<>();
        m.put("menuOptions", menuOptionRepository.findAllByMenuId(menuId));

        return new ResponseDTO<>(HttpStatus.OK.value(), m);
    }
    @PostMapping("/")
    @ApiOperation(value = "새로운 메뉴 옵션을 생성한다.", notes = "새로운 메뉴 옵션 정보를 입력받아 저장한다.")
    public ResponseEntity<ResponseDTO> insertMenuOption(Authentication authentication, @RequestBody MenuOptionDTO menuOptionDTO) {
        MenuOptionDTO createdMenuOption = menuOptionService.insertMenuOption(authentication,menuOptionDTO);
        return new ResponseEntity<>(new ResponseDTO<>(HttpStatus.CREATED.value(), createdMenuOption), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "메뉴 옵션을 수정한다.", notes = "주어진 ID에 해당하는 메뉴 옵션의 정보를 업데이트한다.")
    public ResponseEntity<ResponseDTO> updateMenuOption(Authentication authentication,@PathVariable Long id, @RequestBody MenuOptionDTO menuOptionDTO) {
        MenuOptionDTO updatedMenuOption = menuOptionService.updateMenuOption(authentication,id, menuOptionDTO);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), updatedMenuOption));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "메뉴 옵션을 삭제한다.", notes = "주어진 ID에 해당하는 메뉴 옵션을 삭제한다.")
    public ResponseEntity<ResponseDTO> deleteMenuOption(Authentication authentication,@PathVariable Long id) {
        menuOptionService.deleteMenuOption(authentication,id);
        return ResponseEntity.ok(new ResponseDTO<>(HttpStatus.OK.value(), "Deleted successfully"));
    }
}

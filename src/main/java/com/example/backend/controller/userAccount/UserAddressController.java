package com.example.backend.controller.userAccount;

import com.example.backend.dto.userAccount.UserAccountResponseDto;
import com.example.backend.dto.userAccount.UserAddressDto;
import com.example.backend.service.userAccount.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/user/addresses")
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    @PostMapping
    public ResponseEntity<?> createUserAddress(@RequestBody UserAddressDto userAddressDto, Authentication authentication) {
        Long userAddressId = userAddressService.createUserAddress(userAddressDto, authentication);
        UserAccountResponseDto updated = userAddressService.updateMainAddress(userAddressId, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated); // 새로운 주소가 생성되었음을 나타내는 상태 코드 201과 함께 생성된 주소 정보를 반환
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddressDto> updateUserAddress(@PathVariable Long id, @RequestBody UserAddressDto userAddressDto) {
        UserAddressDto updated = userAddressService.updateUserAddress(id, userAddressDto);
        return ResponseEntity.ok(updated); // 주소가 업데이트되었음을 나타내는 상태 코드 200과 함께 업데이트된 주소 정보를 반환
    }

    @PutMapping("/main-address-change/{id}")
    public ResponseEntity<UserAccountResponseDto> updateMainAddress(@PathVariable Long id, Authentication authentication) {
        UserAccountResponseDto updated = userAddressService.updateMainAddress(id, authentication);
        return ResponseEntity.ok(updated); // 주소가 업데이트되었음을 나타내는 상태 코드 200과 함께 업데이트된 주소 정보를 반환
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Long id) {
        userAddressService.deleteUserAddress(id);
        return ResponseEntity.noContent().build(); // 주소가 성공적으로 삭제되었음을 나타내는 상태 코드 204를 반환
    }

    @GetMapping
    public ResponseEntity<List<UserAddressDto>> getAllUserAddresses(Authentication authentication) {
        List<UserAddressDto> addresses = userAddressService.getAllUserAddresses(authentication);
        return ResponseEntity.ok(addresses); // 주소 목록이 성공적으로 검색되었음을 나타내는 상태 코드 200과 함께 주소 목록을 반환
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAddressDto> getUserAddress(@PathVariable Long id) {
        UserAddressDto address = userAddressService.getUserAddress(id);
        return ResponseEntity.ok(address); // 주소가 성공적으로 검색되었음을 나타내는 상태 코드 200과 함께 주소 정보를 반환
    }
}

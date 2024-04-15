package com.example.backend.controller.userAccount;

import com.example.backend.UserAccount.dto.UserAddressDto;

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
    public ResponseEntity<Long> createUserAddress(@RequestBody UserAddressDto userAddressDto, Authentication authentication) {
        Long userAddressId = userAddressService.createUserAddress(userAddressDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(userAddressId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAddressDto> updateUserAddress(@PathVariable Long id, @RequestBody UserAddressDto userAddressDto) {
        UserAddressDto updated = userAddressService.updateUserAddress(id, userAddressDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAddress(@PathVariable Long id) {
        userAddressService.deleteUserAddress(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserAddressDto>> getAllUserAddresses(Authentication authentication) {
        List<UserAddressDto> addresses = userAddressService.getAllUserAddresses(authentication);
        return ResponseEntity.ok(addresses);
    }
}
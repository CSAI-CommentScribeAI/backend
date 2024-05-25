package com.example.backend.controller.userAccount;


import com.example.backend.dto.userAccount.ChangePasswordRequestDTO;
import com.example.backend.dto.userAccount.UserAccountRequestDTO;
import com.example.backend.dto.userAccount.UserAccountResponseDTO;
import com.example.backend.service.userAccount.UserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/info")
    public ResponseEntity<UserAccountResponseDTO> getMyAccountInfo() {
        UserAccountResponseDTO myInfoBySecurity = userAccountService.getMyInfoBySecurity();
        System.out.println(myInfoBySecurity.getNickname());
        return ResponseEntity.ok(myInfoBySecurity);
    }

    @PostMapping("/nickname")
    public ResponseEntity<UserAccountResponseDTO> changeUserAccountNickname(@RequestBody UserAccountRequestDTO requestDto) {
        return ResponseEntity.ok(userAccountService.changeUserAccountNickname(requestDto.getUserId(), requestDto.getNickname()));
    }

    @PostMapping("/password")
    public ResponseEntity<UserAccountResponseDTO> changeUserAccountPassword(@RequestBody ChangePasswordRequestDTO requestDto, Authentication authentication){
        return ResponseEntity.ok(userAccountService.changeUserAccountPassword(authentication, requestDto.getPassword(), requestDto.getNewPassword()));
    }
}

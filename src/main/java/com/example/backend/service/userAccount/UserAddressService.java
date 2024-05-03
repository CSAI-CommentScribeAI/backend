package com.example.backend.service.userAccount;

import com.example.backend.dto.userAccount.UserAccountResponseDto;
import com.example.backend.dto.userAccount.UserAddressDto;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.UserAccount.UserAddressRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    private final UserAccountRepository userAccountRepository;

    public UserAddressService(UserAddressRepository userAddressRepository, UserAccountRepository userAccountRepository) {
        this.userAddressRepository = userAddressRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public Long createUserAddress(UserAddressDto userAddressDto, Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        UserAddress userAddress = UserAddress.builder()
                .userAccount(userAccount)
                .fullAddress(userAddressDto.getFullAddress())
                .roadAddress(userAddressDto.getRoadAddress())
                .jibunAddress(userAddressDto.getJibunAddress())
                .postalCode(userAddressDto.getPostalCode())
                .latitude(userAddressDto.getLatitude())
                .longitude(userAddressDto.getLongitude())
                .build();

        return userAddressRepository.save(userAddress).getId();
    }

    @Transactional
    public UserAddressDto updateUserAddress(Long id, UserAddressDto userAddressDto) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAddress not found"));
        // Update the entity fields
        userAddress.setFullAddress(userAddressDto.getFullAddress());
        userAddress.setRoadAddress(userAddressDto.getRoadAddress());
        userAddress.setJibunAddress(userAddressDto.getJibunAddress());
        userAddress.setPostalCode(userAddressDto.getPostalCode());
        userAddress.setLatitude(userAddressDto.getLatitude());
        userAddress.setLongitude(userAddressDto.getLongitude());
        userAddressRepository.save(userAddress);
        return userAddressDto;
    }

    @Transactional
    public UserAccountResponseDto updateMainAddress(Long id, Authentication authentication){
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        userAccount.setUserAddress(Math.toIntExact(id));

        return UserAccountResponseDto.of(userAccountRepository.save(userAccount));
    }

    @Transactional
    public void deleteUserAddress(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAddress not found"));
        userAddressRepository.delete(userAddress);
    }

    public List<UserAddressDto> getAllUserAddresses(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount userAccount = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        return userAddressRepository.findByUserAccountId(userAccount.getId()).stream()
                .map(address -> {
                    UserAddressDto dto = new UserAddressDto();
                    // Mapping Entity to DTO
                    dto.setFullAddress(address.getFullAddress());
                    dto.setRoadAddress(address.getRoadAddress());
                    dto.setJibunAddress(address.getJibunAddress());
                    dto.setPostalCode(address.getPostalCode());
                    dto.setLatitude(address.getLatitude());
                    dto.setLongitude(address.getLongitude());
                    return dto;
                }).collect(Collectors.toList());
    }

    public UserAddressDto getUserAddress(Long id) {
        UserAddress userAddress = userAddressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAddress not found"));
        UserAddressDto dto = new UserAddressDto();
        // Mapping Entity to DTO
        dto.setFullAddress(userAddress.getFullAddress());
        dto.setRoadAddress(userAddress.getRoadAddress());
        dto.setJibunAddress(userAddress.getJibunAddress());
        dto.setPostalCode(userAddress.getPostalCode());
        dto.setLatitude(userAddress.getLatitude());
        dto.setLongitude(userAddress.getLongitude());
        return dto;
    }
}

package com.example.backend.service.userAccount;

import com.example.backend.UserAccount.dto.UserAddressDto;
import com.example.backend.UserAccount.entity.UserAccount;
import com.example.backend.UserAccount.entity.UserAddress;
import com.example.backend.UserAccount.repository.UserAccountRepository;
import com.example.backend.UserAccount.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Transactional
    public Long createUserAddress(UserAddressDto userAddressDto, Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication information is not available.");
        }

        long userId = Long.parseLong(authentication.getName());
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        UserAddress userAddress = UserAddress.builder()
                .userAccount(authUser)
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
        UserAccount authUser = userAccountRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with userId: " + userId));

        return userAddressRepository.findByUserAccountId(authUser.getId()).stream()
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
}

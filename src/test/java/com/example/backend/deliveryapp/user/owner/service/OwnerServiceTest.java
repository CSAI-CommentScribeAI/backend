package com.example.backend.deliveryapp.user.owner.service;

import com.example.backend.deliveryapp.service.OwnerService;
import com.example.backend.deliveryapp.user.owner.dto.OwnerCreateRequest;
import com.example.backend.deliveryapp.user.owner.dto.OwnerDetailResponse;
import com.example.backend.deliveryapp.user.owner.dto.OwnerUpdateRequest;
import com.example.backend.deliveryapp.user.owner.entity.Owner;
import com.example.backend.deliveryapp.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    private static Owner owner1;


    @BeforeEach
    void createOwner() {
        OwnerCreateRequest ownerRequest1 = new OwnerCreateRequest("owner1", "owner1@naver.com", "01000000000");
        owner1 = ownerService.createOwner(ownerRequest1);
        assertThat(owner1.getUsername()).isEqualTo("owner1");
    }

    @AfterEach
    void tearDown() {
        ownerRepository.deleteAll();
    }

    @Test
    void findOneOwner() {
        // Given
        // When
        OwnerDetailResponse ownerResponse1 = ownerService.findOneOwner(); // 매개변수 없이 호출
        // Then
        assertThat(ownerResponse1.getUserName()).isEqualTo(owner1.getUsername());
    }

    @Test
    void updateOwner() {
        // Given
        Long owner1Id = owner1.getId();
        OwnerUpdateRequest owner1Request = new OwnerUpdateRequest("updated owner1", "updatedOwner1@naver.com", "01000000000");
        // When
        Owner updatedOwner = ownerService.updateOwner(owner1Request); // 매개변수 없이 호출
        OwnerDetailResponse oneOwner = ownerService.findOneOwner(); // 매개변수 없이 호출
        // Then
        assertThat(updatedOwner.getUsername()).isEqualTo(oneOwner.getUserName());
    }

    @Test
    void deleteOwner() {
        // Given
        // When
        ownerService.deleteOwner(); // 매개변수 없이 호출
        // Then
        assertFalse(ownerRepository.existsById(owner1.getId()));
    }
}

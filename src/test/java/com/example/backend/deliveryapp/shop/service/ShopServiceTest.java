package com.example.backend.deliveryapp.shop.service;

import com.example.backend.deliveryapp.root.exception.EntityExceptionSuppliers;
import com.example.backend.deliveryapp.service.ShopService;
import com.example.backend.deliveryapp.shop.dto.ShopDto;
import com.example.backend.deliveryapp.shop.entity.Shop;
import com.example.backend.deliveryapp.shop.entity.ShopStatus;
import com.example.backend.deliveryapp.shop.entity.ShopSupportedOrderType;
import com.example.backend.deliveryapp.shop.entity.ShopSupportedPayment;
import com.example.backend.deliveryapp.shop.repository.ShopRepository;
import com.example.backend.deliveryapp.user.owner.entity.Owner;
import com.example.backend.deliveryapp.user.owner.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ShopServiceTest {

    @Autowired
    ShopService shopService;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    OwnerRepository ownerRepository;

    Owner owner;
    Shop shop;

    @BeforeEach
    void save_test() {
        owner = new Owner("username", "email", "phoneNum");
        ownerRepository.save(owner);
        shop = shopRepository.save(Shop.builder()
                .name("test")
                .phoneNum("010-1234-5678")
                .shortDesc("shop service test")
                .longDesc("test is success")
                .orderType(ShopSupportedOrderType.BOTH)
                .payment(ShopSupportedPayment.CARD)
                .openTime(LocalTime.now())
                .closeTime(LocalTime.now())
                .deliveryFee(3000)
                .minOrderPrice(15000)
                .shopStatus(ShopStatus.DEACTIVATED)
                .registerNumber("123-456-7890")
                .owner(owner)
                .doroAddress("Doro-ro")
                .doroIndex(123)
                .detailAddress("Seoul")
                .build());
    }

    @Test
    void findShopTest() {
        // When
        ShopDto one = shopService.findShop(shop.getId());

        // Then
        assertThat(one.getRegisterNumber()).isEqualTo(shop.getRegisterNumber());
    }

    @Test
    void deleteShopTest() {
        // When
        shopService.deleteShop(shop.getId(),null);

        // Then
        assertFalse(shopRepository.existsById(shop.getId()));
    }

    @Test
    void updateShopTest() {
        // Given
        ShopDto shopDto = shopService.findShop(shop.getId());
        shopDto.setName("update test");
        shopDto.setLongDescription("updated shop");
        shopDto.setDeliveryFee(2000);
        shopDto.setMinOrderPrice(20000);
        shopDto.setShopStatus(ShopStatus.PAUSED);
        shopDto.setDoroIndex(321);
        shopDto.setDetailAddress("Busan");

        // When
        Long updatedShopId = shopService.updateShop(shop.getId(), shopDto,null);

        // Then
        Shop updated = shopRepository.findById(updatedShopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        assertEquals("update test", updated.getName());
        assertEquals("updated shop", updated.getLongDescription());
        assertEquals(2000, updated.getDeliveryFee());
        assertEquals(20000, updated.getMinOrderPrice());
        assertEquals(ShopStatus.PAUSED, updated.getShopStatus());
        assertEquals(321, updated.getDoroIndex());
        assertEquals("Busan", updated.getDetailAddress());
    }
}

package com.example.backend.service.menu;

import com.example.backend.menu.dto.FoodInformationRequest;
import com.example.backend.menu.entity.Food;
import com.example.backend.menu.repository.FoodRepository;
import com.example.backend.root.exception.EntityExceptionSuppliers;
import com.example.backend.service.aws.S3Service;
import com.example.backend.shop.entity.Shop;
import com.example.backend.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodService {
    private final ShopRepository shopRepository;
    private final FoodRepository foodRepository;
    private final S3Service s3Service;

    public Food createFood(long shopId, FoodInformationRequest request, MultipartFile imageFile) throws IOException {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);

        // S3에 이미지 업로드
        String folderName = "shop/" + shop.getName() + "/menu/";
        String imageUrl = s3Service.uploadFile(imageFile, folderName);

        Food food = Food.builder()
                .name(request.getName())
                .shortDescription(request.getShortDescription()) // 수정된 부분
                .price(request.getPrice())
                .discountType(request.getDiscountType())
                .discountAmount(request.getDiscountAmount())
                .shop(shop)
                .status(request.getStatus())
                .build();

        food.setFoodImage(imageUrl);

        // 저장된 Food 엔티티 반환
        return foodRepository.save(food);
    }

    @Transactional
    public void deleteFood(long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);

        // S3에서 이미지 삭제
        if (food.getFoodImage() != null) {
            s3Service.deleteFile(food.getFoodImage());
        }

        foodRepository.delete(food);
    }

    public Food updateFood(long shopId, Long foodId, FoodInformationRequest request, MultipartFile imageFile) throws IOException {
        Shop shop = shopRepository.findById(shopId).orElseThrow(EntityExceptionSuppliers.shopNotFound);
        Food food = foodRepository.findByIdAndShop(foodId, shop).orElseThrow(EntityExceptionSuppliers.foodNotFound);

        // S3에서 이전 이미지 삭제
        if (food.getFoodImage() != null) {
            s3Service.deleteFile(food.getFoodImage());
        }

        String folderName = "shop/" + shop.getName() + "/menu/";
        String imageUrl = s3Service.uploadFile(imageFile, folderName);

        // 이미지 URL을 엔티티에 설정
        food.setFoodImage(imageUrl);

        // 업데이트 요청 내용 적용
        food.changeName(request.getName());
        food.changeDescription(request.getShortDescription());
        food.changePrice(request.getPrice());
        food.changeDiscountType(request.getDiscountType());
        food.changeDiscountAmount(request.getDiscountAmount());
        food.changeFoodStatus(request.getStatus());

        // 수정된 Food 엔티티 반환
        return foodRepository.save(food);
    }
}

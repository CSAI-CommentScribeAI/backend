package com.example.backend.controller.menu;

import com.example.backend.menu.dto.FoodInformationRequest;
import com.example.backend.menu.entity.Food;
import com.example.backend.root.ApiResponse;
import com.example.backend.service.menu.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("api/v1/shops")
public class MenuController {

    private final FoodService foodService;

    public MenuController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/{shopId}/foods/register")
    public ResponseEntity<ApiResponse<Food>> createFood(
            @PathVariable Long shopId,
            @Valid FoodInformationRequest request,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Long userId = getUserIdFromAuthentication();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized"));
        }

        Food food = foodService.createFood(shopId, request, imageFile);
        return ResponseEntity
                .created(URI.create("/api/v1/shops/foods/" + shopId + "/" + food.getId()))
                .body(ApiResponse.success(food));
    }

    @DeleteMapping("/{shopId}/foods/{foodId}")
    public ResponseEntity<ApiResponse<Void>> deleteFood(
            @PathVariable Long shopId,
            @PathVariable Long foodId) {

        Long userId = getUserIdFromAuthentication();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized"));
        }

        foodService.deleteFood(foodId);
        return ResponseEntity.ok(ApiResponse.success(null, "Food deleted."));
    }

    @PutMapping("/{shopId}/foods/{foodId}")
    public ResponseEntity<ApiResponse<Food>> updateFood(
            @PathVariable Long shopId,
            @PathVariable Long foodId,
            @Valid FoodInformationRequest request,
            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        Long userId = getUserIdFromAuthentication();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized"));
        }

        Food food = foodService.updateFood(shopId, foodId, request, imageFile);
        return ResponseEntity.ok(ApiResponse.success(food));
    }

    private Long getUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return 1L;
    }
}

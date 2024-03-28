package com.example.backend.controller.food;

import com.example.backend.food.entity.dto.bind.FoodInformationRequest;
import com.example.backend.food.entity.dto.bind.FoodDTO;
import com.example.backend.service.food.FoodService;
import com.example.backend.root.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/v1/shops")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping("/{shopId}/foods/register")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> createFood(
            @PathVariable Long shopId,
            @Valid @RequestBody FoodInformationRequest request) {

        Long userId = getUserIdFromAuthentication();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized"));
        }

        List<FoodDTO> food = foodService.createFood(userId, shopId, request);
        return ResponseEntity
                .created(URI.create("/api/v1/shops/foods/" + shopId + "/" + food.get(0).getId()))
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
    public ResponseEntity<ApiResponse<FoodDTO>> updateFood(
            @PathVariable Long shopId,
            @PathVariable Long foodId,
            @Valid @RequestBody FoodInformationRequest request) {

        Long userId = getUserIdFromAuthentication();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.fail("Unauthorized"));
        }

        FoodDTO foodDTO = foodService.updateFood(userId, shopId, foodId, request);
        return ResponseEntity.ok(ApiResponse.success(foodDTO));
    }

    private Long getUserIdFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return 1L; // 실제 사용자 인증 및 권한 로직에 맞게 수정 필요
    }
}

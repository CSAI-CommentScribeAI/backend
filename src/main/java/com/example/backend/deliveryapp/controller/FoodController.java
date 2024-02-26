package com.example.backend.deliveryapp.controller;

import com.example.backend.deliveryapp.controller.bind.FoodInformationRequest;
import com.example.backend.deliveryapp.controller.bind.FoodGroupInformationRequest;
import com.example.backend.deliveryapp.food.entity.dto.FoodDTO;
import com.example.backend.deliveryapp.food.entity.dto.GroupDTO;
import com.example.backend.deliveryapp.service.FoodGroupService;
import com.example.backend.deliveryapp.service.FoodService;
import com.example.backend.deliveryapp.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/shops/{shopId}")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;
    private final FoodGroupService foodGroupService;


    /** food item을 새로 생
     *음식정보들을 요구하는 요청은 param가 하고 나머지
     * 새로 생성한 음식은 기존에 있는 음식 리스트 DTO에 포함될 수 있도록 return
     */
    @PostMapping("/foods")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> createFood(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @Valid @RequestBody FoodInformationRequest request) {
        List<FoodDTO> food = foodService.createFood(shopId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(food));
    }

    /**
     * 기존에 있는 음식 item을 삭제
     * @param음식 아이디에 해당하는 음식을 삭제
     * @return하여 기존에 있는 음식 리스트는 유지되게 한다.
     */
    @DeleteMapping("/foods/{foodId}")
    public ResponseEntity<ApiResponse<List<FoodDTO>>> deleteFood(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId) {
        List<FoodDTO> foodDTOS = foodService.deleteFood(foodId);
        return ResponseEntity.ok(ApiResponse.success(foodDTOS));
    }

    /**
     * Update existing food item.
     * 데이터에 존재하는 음식 item을 수정한다.
     * 수정은 foodId를 기준으로 수정할 수 있도록 한다.
     * @return  기존에 있는 음식 리스트는 유지되게 한다.
     */
    @PutMapping("/foods/{foodId}")
    public ResponseEntity<ApiResponse<FoodDTO>> updateFood(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable(name = "foodId") long foodId,
            @Valid @RequestBody FoodInformationRequest request) {
        FoodDTO foodDTO = foodService.updateFood(shopId, foodId, request);
        return ResponseEntity.ok(ApiResponse.success(foodDTO));
    }


    /**
     * 새로운 food group을 생성
     */
    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> createFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @RequestBody @Valid FoodGroupInformationRequest request) {
        List<GroupDTO> foodGroup = foodGroupService.createFoodGroup(shopId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(foodGroup));
    }

    /**
     * food group에서 기존에 존재하고 있는 데이터 수정
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<GroupDTO>> updateFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long groupId,
            @Valid @RequestBody FoodGroupInformationRequest request) {
        GroupDTO groupDTO = foodGroupService.updateFoodGroup(groupId, request);
        return ResponseEntity.ok(ApiResponse.success(groupDTO));
    }

    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> deleteFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long groupId) {
        List<GroupDTO> groupDTOS = foodGroupService.deleteFoodGroup(groupId);
        return ResponseEntity.ok(ApiResponse.success(groupDTOS));
    }

    @PutMapping("/groups/{groupId}/foods/{foodId}")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> joinFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId) {
        List<GroupDTO> groupDTOS = foodGroupService.joinFoodGroup(foodId, groupId);
        return ResponseEntity.ok(ApiResponse.success(groupDTOS));
    }

    @DeleteMapping("/groups/{groupId}/foods/{foodId}")
    public ResponseEntity<ApiResponse<List<GroupDTO>>> withdrawFoodGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId) {
        List<GroupDTO> groupDTOS = foodGroupService.withdrawFoodGroup(foodId, groupId);
        return ResponseEntity.ok(ApiResponse.success(groupDTOS));
    }

}

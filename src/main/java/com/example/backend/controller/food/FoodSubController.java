package com.example.backend.controller.food;

import com.example.backend.food.entity.dto.bind.FoodSubInformationRequest;
import com.example.backend.food.entity.dto.bind.FoodSubSelectGroupInformationRequest;
import com.example.backend.food.entity.dto.bind.FoodSubDTO;
import com.example.backend.food.entity.dto.bind.FoodSubSelectGroupDTO;
import com.example.backend.service.food.FoodSubSelectGroupService;
import com.example.backend.service.food.FoodSubService;
import com.example.backend.root.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/owners/{ownerId}/shops/{shopId}/foods/{foodId}/sub")
@RequiredArgsConstructor
public class FoodSubController {
    private final FoodSubService foodSubService;
    private final FoodSubSelectGroupService foodSubSelectGroupService;


    /**
     * 새로운 서브 푸드를 create,
     * @param shopId 상점의 ID, @param foodId 이 서브 메뉴를 소유하는 음식의 ID
     * @param request 생성할 서브 푸드의 정보
     * @return 생성된 서브 푸드의 DTO
     */
    @PostMapping
    public ResponseEntity<ApiResponse<List<FoodSubDTO>>> createSubFood(@PathVariable long ownerId,
                                                                       @PathVariable long shopId,
                                                                       @PathVariable long foodId,
                                                                       @Valid @RequestBody FoodSubInformationRequest request) {
        List<FoodSubDTO> foodSub = foodSubService.createFoodSub(shopId, foodId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(foodSub));
    }

    /**
     * 서브 푸드를 삭제
     * @param ownerId 상점 소유주의 ID,@param shopId 상점의 ID
     * @param foodId 음식의 ID,@param subId 서브 푸드의 ID
     * @return 삭제된 푸드를 제외한 서브 푸드 DTO
     */
    @DeleteMapping("/{subId}")
    public ResponseEntity<ApiResponse<List<FoodSubDTO>>> deleteSubFood(@PathVariable long ownerId,
                                                                       @PathVariable long shopId,
                                                                       @PathVariable long foodId,
                                                                       @PathVariable long subId) {
        List<FoodSubDTO> foodSubDTOS = foodSubService.deleteFoodSub(subId);
        return ResponseEntity.ok(ApiResponse.success(foodSubDTOS));
    }

    /**
     * 서브 푸드의 정보를 update
     * @param ownerId 상점 소유주의 ID
     * @param shopId 상점의 ID,@param foodId 음식의 ID, @param subId 서브 푸드의 ID
     * @param request 업데이트된 서브 푸드의 정보
     * @return 업데이트된 서브 푸드의 DTO
     */
    @PutMapping("/{subId}")
    public ResponseEntity<ApiResponse<FoodSubDTO>> updateSubFood(@PathVariable long ownerId,
                                                                 @PathVariable long shopId,
                                                                 @PathVariable long foodId,
                                                                 @PathVariable long subId,
                                                                 @Valid @RequestBody FoodSubInformationRequest request) {
        FoodSubDTO foodSubDTO = foodSubService.updateFoodSub(subId, request);
        return ResponseEntity.ok(ApiResponse.success(foodSubDTO));
    }


    /**
     * 새로운 서브 푸드 그룹을 creat
     * @param ownerId 상점 소유주의 ID
     * @param shopId 상점의 ID, @param foodId 음식의 ID
     * @param request 생성할 서브 푸드 그룹의 정보
     * @return 서브 푸드 그룹의 DTO
     */
    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<List<FoodSubSelectGroupDTO>>> createFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @Valid @RequestBody FoodSubSelectGroupInformationRequest request) {
        List<FoodSubSelectGroupDTO> selectGroup = foodSubSelectGroupService.createSelectGroup(foodId, request);
        return ResponseEntity
                .created(URI.create("/not/available/now"))
                .body(ApiResponse.success(selectGroup));
    }

    /**
     * 서브 푸드 그룹의 정보를 update
     *
     * @param ownerId 상점 소유주의 ID, @param shopId 상점의 ID
     * @param foodId 음식의 ID, @param request 업데이트된 서브 푸드 그룹의 정보
     * @return 업데이트된 서브 푸드 그룹의 DTO
     */
    @PutMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<FoodSubSelectGroupDTO>> updateFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId,
            @Valid @RequestBody FoodSubSelectGroupInformationRequest request) {
        FoodSubSelectGroupDTO foodSubSelectGroupDTO = foodSubSelectGroupService.updateSelectGroup(groupId, request);
        return ResponseEntity.ok(ApiResponse.success(foodSubSelectGroupDTO));
    }

    /**
     * 서브 푸드 그룹을 delete
     * @param ownerId 상점 소유주, @param shopId 상점의 ID
     * @param foodId 음식의 ID, @param groupId 서브 푸드 그룹의 ID
     * @return 삭제된 그룹을 제외한 서브 푸드 그룹 DTO
     */
    @DeleteMapping("/groups/{groupId}")
    public ResponseEntity<ApiResponse<List<FoodSubSelectGroupDTO>>> deleteFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long groupId) {
        List<FoodSubSelectGroupDTO> foodSubSelectGroupDTOS = foodSubSelectGroupService.deleteSelectGroup(groupId);
        return ResponseEntity.ok(ApiResponse.success(foodSubSelectGroupDTOS));
    }


    /**
     * 서브 푸드를 그룹에 add
     * @param ownerId 상점 소유주의 ID, @param shopId 상점의 ID
     * @param foodId 음식의 ID, @param subId 서브 푸드의 ID
     * @param groupId 추가할 서브 푸드 그룹의 ID, @return 추가된 그룹의 서브 푸드 DTO 목록
     */
    @PutMapping("/{subId}/groups/{groupId}")
    public ResponseEntity<ApiResponse<List<FoodSubDTO>>> joinFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long subId,
            @PathVariable long groupId) {
        List<FoodSubDTO> foodSubDTOS = foodSubSelectGroupService.joinSelectGroup(subId, groupId);
        return ResponseEntity.ok(ApiResponse.success(foodSubDTOS));
    }

    /**
     * 서브 푸드를 그룹에서 delete
     * @param ownerId 상점 소유주의 ID
     * @param shopId 상점의 ID
     * @param foodId 음식의 ID
     * @param subId 서브 푸드의 ID
     * @return null
     */
    @DeleteMapping("/{subId}/groups") // 그룹이 M:N이 아니므로 그룹 ID가 필요X
    public ResponseEntity<ApiResponse<List<FoodSubSelectGroupDTO>>> withdrawFoodSubGroup(
            @PathVariable long ownerId,
            @PathVariable long shopId,
            @PathVariable long foodId,
            @PathVariable long subId) {
        List<FoodSubSelectGroupDTO> foodSubSelectGroupDTOS = foodSubSelectGroupService.withdrawSelectGroup(subId);
        return ResponseEntity.ok(ApiResponse.success(foodSubSelectGroupDTOS));
    }
}

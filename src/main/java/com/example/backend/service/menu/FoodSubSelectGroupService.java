package com.example.backend.service.menu;

import com.example.backend.menu.dto.FoodSubSelectGroupInformationRequest;
import com.example.backend.menu.entity.Food;
import com.example.backend.menu.entity.FoodSub;
import com.example.backend.menu.entity.FoodSubSelectGroup;
import com.example.backend.menu.dto.FoodSubDTO;
import com.example.backend.menu.dto.FoodSubSelectGroupDTO;
import com.example.backend.menu.repository.FoodRepository;
import com.example.backend.menu.repository.FoodSubRepository;
import com.example.backend.menu.repository.FoodSubSelectGroupRepository;
import com.example.backend.root.exception.EntityExceptionSuppliers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FoodSubSelectGroupService {
    private final FoodRepository foodRepository;
    private final FoodSubRepository foodSubRepository;
    private final FoodSubSelectGroupRepository foodSubSelectGroupRepository;


    public List<FoodSubSelectGroupDTO> getSelectGroups(long foodId) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        return foodSubSelectGroupRepository.findAllByParentFood(food).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubSelectGroupDTO> createSelectGroup(long foodId, FoodSubSelectGroupInformationRequest request) {
        Food food = foodRepository.findById(foodId).orElseThrow(EntityExceptionSuppliers.foodNotFound);
        FoodSubSelectGroup group = FoodSubSelectGroup.builder()
                .groupName(request.getGroupName())
                .multiSelect(request.isMultiSelect())
                .required(request.isRequired())
                .food(food).build();
        foodSubSelectGroupRepository.save(group);
        return foodSubSelectGroupRepository.findAllByParentFood(food).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public FoodSubSelectGroupDTO updateSelectGroup(long groupId, FoodSubSelectGroupInformationRequest request) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        group.changeGroupName(request.getGroupName());
        group.changeMultiSelect(request.isMultiSelect());
        group.changeRequired(request.isRequired());
        return new FoodSubSelectGroupDTO(group);
    }

    public List<FoodSubSelectGroupDTO> deleteSelectGroup(long groupId) {
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        group.getFoods().forEach(foodSub ->
            foodSub.changeSelectGroup(null) // null로 연관관계를 끊어주지 않으면 constraint violation.
        );
        foodSubSelectGroupRepository.delete(group);
        return foodSubSelectGroupRepository.findAllByParentFood(group.getParentFood()).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubDTO> joinSelectGroup(long foodSubId, long groupId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        FoodSubSelectGroup group = foodSubSelectGroupRepository.findById(groupId).orElseThrow(EntityExceptionSuppliers.foodSubSelectGroupNotFound);
        foodSub.changeGroup(group);
        return group.getFoods().stream()
                .map(FoodSubDTO::new)
                .collect(Collectors.toList());
    }

    public List<FoodSubSelectGroupDTO> withdrawSelectGroup(long foodSubId) {
        FoodSub foodSub = foodSubRepository.findById(foodSubId).orElseThrow(EntityExceptionSuppliers.foodSubNotFound);
        foodSub.withdrawGroup();
        return foodSubSelectGroupRepository.findAllByParentFood(foodSub.getFood()).stream()
                .map(FoodSubSelectGroupDTO::new)
                .collect(Collectors.toList());
    }
}

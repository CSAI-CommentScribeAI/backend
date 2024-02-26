// OwnerController.java

package com.example.backend.deliveryapp.controller;

import com.example.backend.deliveryapp.root.ApiResponse;
import com.example.backend.deliveryapp.user.owner.dto.OwnerCreateRequest;
import com.example.backend.deliveryapp.user.owner.dto.OwnerDetailResponse;
import com.example.backend.deliveryapp.user.owner.dto.OwnerUpdateRequest;
import com.example.backend.deliveryapp.user.owner.entity.Owner;
import com.example.backend.deliveryapp.service.OwnerService;
import javassist.NotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
public class OwnerController {
    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse<String> notFoundHandler (NotFoundException e) {
        return ApiResponse.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> internalServerErrorHandler (Exception e) {
        return ApiResponse.fail(e.getMessage());
    }

    @PostMapping
    public ApiResponse<Owner> createOwner(
            @RequestBody OwnerCreateRequest request
    ) {
        Owner newOwner = ownerService.createOwner(request);
        return ApiResponse.success(newOwner);
    }

    @GetMapping
    public ApiResponse<OwnerDetailResponse> getOwner()  {
        OwnerDetailResponse owner = ownerService.findOneOwner(); // Owner ID 노출하지 않음
        return ApiResponse.success(owner);
    }

    @PutMapping
    public ApiResponse<Owner> updateOwner(
            @RequestBody OwnerUpdateRequest request
    )  {
        Owner owner = ownerService.updateOwner(request); // Owner ID 노출하지 않음
        return ApiResponse.success(owner);
    }

    @DeleteMapping
    public ApiResponse<Void> deleteOwner() {
        ownerService.deleteOwner(); // Owner ID 노출하지 않음
        return ApiResponse.success(null, "Owner deleted.");
    }
}

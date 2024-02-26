package com.example.backend.deliveryapp.service;

import com.example.backend.deliveryapp.root.exception.EntityExceptionSuppliers;
import com.example.backend.deliveryapp.user.owner.dto.OwnerCreateRequest;
import com.example.backend.deliveryapp.user.owner.dto.OwnerDetailResponse;
import com.example.backend.deliveryapp.user.owner.dto.OwnerUpdateRequest;
import com.example.backend.deliveryapp.user.owner.entity.Owner;
import com.example.backend.deliveryapp.user.owner.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Transactional
    public Owner createOwner(OwnerCreateRequest request) {
        Owner owner = new Owner(request.getUserName(),
                request.getEmail(),
                request.getPhoneNum());
        return ownerRepository.save(owner);
    }

    @Transactional
    public OwnerDetailResponse findOneOwner() {
        // 적절한 Owner 정보를 조회하는 로직 추가
        // 예시로 첫 번째 Owner를 조회하는 코드를 작성합니다.
        Owner owner = ownerRepository.findAll().stream().findFirst().orElseThrow(EntityExceptionSuppliers.ownerNotFound);
        return new OwnerDetailResponse(owner);
    }

    @Transactional
    public Owner updateOwner(OwnerUpdateRequest request) {
        // 적절한 Owner 정보를 업데이트하는 로직 추가
        // 예시로 첫 번째 Owner를 업데이트하는 코드를 작성합니다.
        Owner owner = ownerRepository.findAll().stream().findFirst().orElseThrow(EntityExceptionSuppliers.ownerNotFound);
        owner.changeName(request.getUserName());
        owner.changeEmail(request.getEmail());
        owner.changePhoneNum(request.getPhoneNum());
        owner.changeUpdatedAt(request.getUpdatedAt());
        return owner;
    }

    @Transactional
    public void deleteOwner() {
        // 적절한 Owner 정보를 삭제하는 로직 추가
        // 예시로 첫 번째 Owner를 삭제하는 코드를 작성합니다.
        Owner owner = ownerRepository.findAll().stream().findFirst().orElseThrow(EntityExceptionSuppliers.ownerNotFound);
        ownerRepository.delete(owner);
    }
}

package com.example.backend.service.store;

import com.example.backend.dto.store.*;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.exception.store.AlreadyStoreBossAssignException;
import com.example.backend.exception.store.StoreNotFoundException;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.store.StoreRepository;
import com.example.backend.service.aws.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final AwsS3Service s3Service;
    public static StoreSearchResponseDTO entityToDTO(Store s){
        return StoreSearchResponseDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .minOrderPrice(s.getMinOrderPrice())
                .category(s.getCategory())
                .info(s.getInfo())
                .userAddress(s.getUserAddress())
                .storeImageUrl(s.getStoreImageUrl())
                .build();
    }


    @Transactional
    public StoreDTO insert(Authentication authentication, StoreInsertDTO storeDTO, MultipartFile multipartFile) {
        String userId = authentication.getName();

        // 사용자 계정을 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 이미지를 S3에 업로드합니다.
        String imgPath = s3Service.uploadStoreImage("store", multipartFile);

        // 매장 엔티티를 생성합니다.
        Store store = Store.builder()
                .name(storeDTO.getName())
                .minOrderPrice(storeDTO.getMinOrderPrice())
                .storeImageUrl(imgPath)
                .category(storeDTO.getCategory())
                .info(storeDTO.getInfo())
                .businessLicense(storeDTO.getBusinessLicense())
                .userAccount(userAccount)
                .build();

        // 매장을 저장합니다.
        store = storeRepository.save(store);

        return StoreDTO.entityToDTO(store);
    }

    public Optional<StoreDTO> findById(Long id) {
        Optional<Store> storeOptional = storeRepository.findById(id);
        return storeOptional.map(StoreDTO::entityToDTO);
    }

    public Page<StoreDTO> selectAll(Pageable pageable) {
        return storeRepository.findAll(pageable)
                .map(StoreDTO::entityToDTO);
    }

    public List<StoreDTO> selectCategory(CategoryDTO categoryDTO) {
        return storeRepository.findByCategory(categoryDTO.getCategory())
                .stream()
                .map(StoreDTO::entityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StoreDTO update(Authentication authentication, Long storeId, StoreInsertDTO storeDTO) {
        // 사용자의 인증 정보에서 사용자 ID를 가져옵니다.
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외를 던집니다.
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new AlreadyStoreBossAssignException();
        }

        // Store 엔티티를 데이터베이스에서 조회합니다.
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        // Store 엔티티를 업데이트합니다.
        store.setName(storeDTO.getName());
        store.setMinOrderPrice(storeDTO.getMinOrderPrice());
        store.setCategory(storeDTO.getCategory());
        store.setInfo(storeDTO.getInfo());

        store = storeRepository.save(store);

        return StoreDTO.entityToDTO(store);
    }

    @Transactional
    public void delete(Authentication authentication, Long storeId) {
        // 사용자의 인증 정보에서 사용자 ID를 가져옵니다.
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외를 던집니다.
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new AlreadyStoreBossAssignException();
        }

        // Store 엔티티를 데이터베이스에서 조회합니다.
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new StoreNotFoundException());

        // Store 엔티티를 삭제합니다.
        storeRepository.delete(store);
    }

    public List<StoreSearchResponseDTO> search(StoreSearchReqeustDTO ssrDTO) {
        return storeRepository.findByNameContainingIgnoreCase(ssrDTO.getKeyword())
                .stream()
                .map(StoreSearchResponseDTO::entityToDTO)
                .collect(Collectors.toList());
    }
}

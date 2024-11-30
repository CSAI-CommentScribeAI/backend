package com.example.backend.service.store;

import com.example.backend.dto.store.*;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.store.StoreAddress;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserAddress;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.exception.store.AlreadyStoreBossAssignException;
import com.example.backend.exception.store.StoreNotFoundException;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.UserAccount.UserAddressRepository;
import com.example.backend.repository.store.StoreAddressRepository;
import com.example.backend.repository.store.StoreRepository;
import com.example.backend.service.aws.AwsS3Service;
import java.util.ArrayList;
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
@Transactional
public class StoreService {

    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final AwsS3Service s3Service;
    private final StoreAddressRepository storeAddressRepository;
    private final UserAddressRepository userAddressRepository;

    public static StoreSearchResponseDTO entityToDTO(Store s){
        return StoreSearchResponseDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .minOrderPrice(s.getMinOrderPrice())
                .category(s.getCategory())
                .info(s.getInfo())
                .storeAddress(s.getStoreAddress())
                .storeImageUrl(s.getStoreImageUrl())
                .build();
    }
    @Transactional
    public StoreDTO insert(Authentication authentication, StoreInsertDTO storeDTO, MultipartFile multipartFile) {
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        String imgPath = s3Service.uploadStoreImage("store", multipartFile);

        // Store를 먼저 저장하고 ID를 받아옴
        Store store = Store.builder()
                .name(storeDTO.getName())
                .minOrderPrice(storeDTO.getMinOrderPrice())
                .storeImageUrl(imgPath)
                .category(storeDTO.getCategory())
                .info(storeDTO.getInfo())
                .businessLicense(storeDTO.getBusinessLicense())
                .openTime(storeDTO.getOpenTime())
                .closeTime(storeDTO.getCloseTime())
                .userAccount(userAccount)
                .build();

        store = storeRepository.save(store);

        // Store 엔티티의 ID를 사용하여 StoreAddress를 생성하여 저장
        StoreAddress storeAddress = StoreAddress.builder()
                .fullAddress(storeDTO.getFullAddress())
                .roadAddress(storeDTO.getRoadAddress())
                .jibunAddress(storeDTO.getJibunAddress())
                .postalCode(storeDTO.getPostalCode())
                .latitude(storeDTO.getLatitude())
                .longitude(storeDTO.getLongitude())
                .store(store) // Store 엔티티와 연결
                .build();

        // StoreAddress 엔티티를 저장
        storeAddress = storeAddressRepository.save(storeAddress);

        // Store 엔티티에 StoreAddress의 ID를 설정하여 매핑
        store.setStoreAddress(storeAddress);
        userAccount.setStore(store); // 사용자 계정에 매장 엔티티 설정

        // 사용자 계정 업데이트
        userAccountRepository.save(userAccount);

        return StoreDTO.entityToDTO(store);
    }

    public Optional<StoreDTO> findById(Long id) {
        Optional<Store> storeOptional = storeRepository.findById(id);
        return storeOptional.map(StoreDTO::entityToDTO);
    }

    public Page<StoreDTO> findStoresWithin5Km(Pageable pageable, Authentication authentication) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));
        UserAddress userAddress = userAddressRepository.findById((long) userAccount.getAddress())
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자 주소를 찾을 수 없습니다: " + userAccount.getAddress()));
        System.out.println("고객 위도 : " + userAddress.getLatitude());
        System.out.println("고객 경도 : " + userAddress.getLongitude());
        return storeRepository.findStoresWithinDistance(userAddress.getLatitude(),userAddress.getLongitude(), 5.0,pageable)
                .map(StoreDTO::entityToDTO);
    }

    public List<StoreDTO> selectCategory(CategoryDTO categoryDTO, Authentication authentication) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));
        UserAddress userAddress = userAddressRepository.findById((long) userAccount.getAddress())
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자 주소를 찾을 수 없습니다: " + userAccount.getAddress()));
        return storeRepository.findStoresWithinDistanceAndCategory(userAddress.getLatitude(),userAddress.getLongitude(), 5.0,categoryDTO.getCategory())
                .stream()
                .map(StoreDTO::entityToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StoreDTO update(Authentication authentication, Long storeId, StoreInsertDTO storeDTO) {
        String userId = authentication.getName();

        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new AlreadyStoreBossAssignException();
        }

        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        store.setName(storeDTO.getName());
        store.setMinOrderPrice(storeDTO.getMinOrderPrice());
        store.setCategory(storeDTO.getCategory());
        store.setInfo(storeDTO.getInfo());
        store.setOpenTime(storeDTO.getOpenTime());
        store.setCloseTime(storeDTO.getCloseTime());

        storeRepository.save(store);

        return StoreDTO.entityToDTO(store);
    }


    @Transactional
    public void delete(Authentication authentication, Long storeId) {
        // 사용자의 인증 정보에서 사용자 ID를 가져옵니다.
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회합니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외를 던집니다.
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new AlreadyStoreBossAssignException();
        }

        // Store 엔티티를 데이터베이스에서 조회합니다.
        Store store = storeRepository.findById(storeId)
                .orElseThrow(StoreNotFoundException::new);

        // Store 엔티티를 삭제합니다.
        storeRepository.delete(store);
    }

    public List<StoreSearchResponseDTO> search(StoreSearchReqeustDTO ssrDTO) {
        return storeRepository.findByNameContainingIgnoreCase(ssrDTO.getKeyword())
                .stream()
                .map(StoreSearchResponseDTO::entityToDTO)
                .collect(Collectors.toList());
    }

    public List<StoreOwnerDTO> findStoresByOwner(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null.");
        }
        String userId = authentication.getName();

        // ID로 UserAccount 객체를 찾아옵니다.
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("사장님이 아닙니다: " + userId);
        }

        // findByUserAccount 메소드에 UserAccount 객체를 전달합니다.
        List<Store> stores = storeRepository.findByUserAccount(userAccount);
        return stores.stream()
                .map(StoreOwnerDTO::entityToDTO)
                .collect(Collectors.toList());
    }

    public Object createStoreList(Authentication authentication, List<StoreListDTO> storeListDTO) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        List<Store> stores = storeListDTO.stream()
                .map(storeDTO -> Store.builder()
                        .id(null)
                        .userAccount(userAccount)
                        .name(storeDTO.getName())
                        .minOrderPrice(storeDTO.getMinOrderPrice())
                        .storeImageUrl(null)
                        .category(storeDTO.getCategory())
                        .info(storeDTO.getInfo())
                        .openTime(storeDTO.getOpenTime())
                        .closeTime(storeDTO.getCloseTime())
                        .businessLicense(storeDTO.getBusinessLicense())
                        .build())
                .collect(Collectors.toList());

        stores = storeRepository.saveAll(stores);

        List<StoreAddress> storeAddresses = new ArrayList<>();
        for (int i = 0; i < storeListDTO.size(); i++) {
            Store store = stores.get(i);
            StoreListDTO storeDTO = storeListDTO.get(i);
            if (storeDTO.getRoadAddress() == null) {
                throw new IllegalArgumentException("Road address cannot be null for store: " + storeDTO.getName());
            }
            StoreAddress storeAddress = StoreAddress.builder()
                    .fullAddress(storeDTO.getFullAddress())
                    .roadAddress(storeDTO.getRoadAddress())
                    .jibunAddress(storeDTO.getJibunAddress())
                    .postalCode(storeDTO.getPostalCode())
                    .latitude(storeDTO.getLatitude())
                    .longitude(storeDTO.getLongitude())
                    .store(store)
                    .build();
            storeAddresses.add(storeAddress);
            // Store 엔티티에 StoreAddress의 ID를 설정하여 매핑
            store.setStoreAddress(storeAddress);

        }

        storeAddressRepository.saveAll(storeAddresses);

        return stores.stream()
                .map(StoreDTO::new)
                .collect(Collectors.toList()); // 반환 타입에 맞게 수정
    }




}

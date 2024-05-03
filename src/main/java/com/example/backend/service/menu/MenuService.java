package com.example.backend.service.menu;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.menu.MenuDTO;
import com.example.backend.dto.menu.MenuInsertDTO;
import com.example.backend.dto.menu.MenuUpdateDTO;
import com.example.backend.dto.store.StoreDTO;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.menu.MenuOptionTitle;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.exception.menu.MenuNotFoundException;
import com.example.backend.exception.store.StoreNotFoundException;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.menu.MenuConnectRepository;
import com.example.backend.repository.menu.MenuOptionRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.repository.store.StoreRepository;
import com.example.backend.service.aws.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final MenuConnectRepository menuConnectRepository;
    private final AwsS3Service s3Service;

    public ResponseDTO<?> selectMenu(Long menuId){

        Map<String, Object> m = new HashMap<>();

        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);

        List<MenuOptionTitle> motList = menuConnectRepository.findByMenu(menu);
        for(MenuOptionTitle mot : motList) {
            mot.setMenuOptionList(menuOptionRepository.findAllByMenuOptionTitle(mot));
        }

        m.put("menu", MenuDTO.entityToDTO(menu));
        m.put("menuOptionTitle", motList);

        return new ResponseDTO<>(HttpStatus.OK.value(), m);
    }

    @Transactional
    public StoreDTO addMenu(Authentication authentication, MenuInsertDTO menuDTO, MultipartFile multipartFile){
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("가게를 등록할 권한이 없습니다.");
        }

        // 매장 조회
        Store store = storeRepository.findById(Long.valueOf(userId)).orElseThrow(StoreNotFoundException::new);

        // 이미지를 S3에 업로드
        String imageUrl = s3Service.uploadMenuImage("menu", multipartFile);

        // 메뉴 생성 및 저장
        Menu menu = menuDTO.dtoToEntity();
        menu.setImageUrl(imageUrl); // 이미지 경로 설정
        menu.setStore(store);
        menuRepository.save(menu);

        return StoreDTO.entityToDTO(store);
    }

    @Transactional
    public StoreDTO updateMenu(Authentication authentication, Long menuId, MenuUpdateDTO menuDTO){
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("가게를 등록할 권한이 없습니다.");
        }

        // 매장 조회
        Store store = storeRepository.findById(Long.valueOf(userId)).orElseThrow(StoreNotFoundException::new);

        // 메뉴 조회
        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);

        // 메뉴가 해당 매장에 속하는지 확인
        if(!store.equals(menu.getStore())) {
            throw new IllegalArgumentException("메뉴가 해당 가게에 속하지 않습니다.");
        }

        // 메뉴 정보 업데이트
        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        menu.setImageUrl(menuDTO.getImageUrl());
        menu.setMenuDetail(menuDTO.getMenuDetail());

        return StoreDTO.entityToDTO(store);
    }

    @Transactional
    public String deleteMenu(Authentication authentication, Long menuId){
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("ID에 해당하는 사용자를 찾을 수 없습니다: " + userId));

        // 사용자의 역할이 ROLE_OWNER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_OWNER) {
            throw new IllegalStateException("가게를 등록할 권한이 없습니다.");
        }

        // 메뉴 조회
        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);

        // 메뉴를 매장에서 삭제
        menu.getStore().deleteMenu(menu);

        // 메뉴 삭제
        try {
            menuRepository.delete(menu);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("메뉴 삭제 중 오류가 발생했습니다.", e);
        }

        return "메뉴가 삭제되었습니다.";
    }
}

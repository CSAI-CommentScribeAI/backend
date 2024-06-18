package com.example.backend.service.menu.impl;

import com.example.backend.dto.menu.MenuRequestDTO;
import com.example.backend.dto.menu.MenuResponseDTO;
import com.example.backend.entity.menu.Menu;
import com.example.backend.entity.menu.MenuStatus;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.exception.menu.MenuNotFoundException;
import com.example.backend.exception.store.StoreNotFoundException;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.menu.MenuRepository;
import com.example.backend.repository.store.StoreRepository;
import com.example.backend.service.aws.AwsS3Service;
import com.example.backend.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final AwsS3Service s3Service;

    @Transactional
    public MenuResponseDTO addMenu(Authentication authentication, MenuRequestDTO menuDTO, MultipartFile multipartFile) {
        UserAccount userAccount = getUserAccount(authentication);
        Store store = storeRepository.findById(menuDTO.getStoreId()).orElseThrow(StoreNotFoundException::new);
        if (!store.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("메뉴를 추가할 권한이 없습니다.");
        }
        String imageUrl = uploadMenuImage(multipartFile);
        Menu menu = Menu.builder()
                .name(menuDTO.getName())
                .price(menuDTO.getPrice())
                .menuDetail(menuDTO.getMenuDetail())
                .imageUrl(imageUrl)
                .status(menuDTO.getStatus())
                .store(store)
                .build();
        menuRepository.save(menu);
        return MenuResponseDTO.fromEntity(menu);
    }

    @Transactional
    public MenuResponseDTO updateMenu(Authentication authentication, Long menuId, MenuRequestDTO menuDTO, MultipartFile multipartFile) {
        UserAccount userAccount = getUserAccount(authentication);
        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);
        if (!menu.getStore().getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("메뉴를 업데이트할 권한이 없습니다.");
        }
        updateMenuDetails(menu, menuDTO, multipartFile);
        return MenuResponseDTO.fromEntity(menu);
    }

    @Transactional
    public String deleteMenu(Authentication authentication, Long menuId) {
        UserAccount userAccount = getUserAccount(authentication);
        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);
        if (!menu.getStore().getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("메뉴 삭제 권한이 없습니다.");
        }
        menuRepository.delete(menu);
        return "메뉴가 삭제되었습니다.";
    }

    public List<MenuResponseDTO> findMenusByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream()
                .map(MenuResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MenuResponseDTO> createMenuList(List<MenuRequestDTO> menuRequestDTOs) {
        List<Menu> menus = menuRequestDTOs.stream()
                .map(this::requestDtoToEntity)
                .collect(Collectors.toList());
        List<Menu> savedMenus = menuRepository.saveAll(menus);

        return savedMenus.stream()
                .map(MenuResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private Menu requestDtoToEntity(MenuRequestDTO menuRequestDTO) {
        Store store = storeRepository.findById(menuRequestDTO.getStoreId())
                .orElseThrow(StoreNotFoundException::new);
        return Menu.builder()
                .store(store)
                .name(menuRequestDTO.getName())
                .price(menuRequestDTO.getPrice())
                .menuDetail(menuRequestDTO.getMenuDetail())
                .imageUrl(menuRequestDTO.getImageUrl())
                .status(MenuStatus.SALE)
                .build();
    }

    private void updateMenuDetails(Menu menu, MenuRequestDTO menuDTO, MultipartFile multipartFile) {
        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        menu.setMenuDetail(menuDTO.getMenuDetail());
        menu.setStatus(menuDTO.getStatus());
        if (multipartFile != null && !multipartFile.isEmpty()) {
            String imageUrl = uploadMenuImage(multipartFile);
            menu.setImageUrl(imageUrl);
        }
        menuRepository.save(menu);
    }

    private String uploadMenuImage(MultipartFile multipartFile) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 제공되지 않았습니다.");
        }
        return s3Service.uploadMenuImage("menu", multipartFile);
    }

    private UserAccount getUserAccount(Authentication authentication) {
        String userId = authentication.getName();
        return userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("사용자 계정을 찾을 수 없습니다: " + userId));
    }
}

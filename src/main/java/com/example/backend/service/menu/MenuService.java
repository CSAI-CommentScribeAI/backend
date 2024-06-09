package com.example.backend.service.menu;

import com.example.backend.dto.menu.MenuDTO;
import com.example.backend.dto.menu.MenuInsertDTO;
import com.example.backend.dto.menu.MenuListDTO;
import com.example.backend.dto.menu.MenuUpdateDTO;
import com.example.backend.dto.store.StoreDTO;
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
public class MenuService {

    private final MenuRepository menuRepository;
    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final AwsS3Service s3Service;

    @Transactional
    public StoreDTO addMenu(Authentication authentication, MenuInsertDTO menuDTO, MultipartFile multipartFile, Long storeId) {
        UserAccount userAccount = getUserAccount(authentication);
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
        if (!store.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("메뉴를 추가할 권한이 없습니다.");
        }
        String imageUrl = uploadMenuImage(multipartFile);
        Menu menu = menuDTO.dtoToEntity();
        menu.setImageUrl(imageUrl);
        menu.setStore(store);
        menuRepository.save(menu);
        return StoreDTO.entityToDTO(store);
    }

    @Transactional
    public StoreDTO updateMenu(Authentication authentication, Long storeId, Long menuId, MenuUpdateDTO menuDTO, MultipartFile multipartFile){
        UserAccount userAccount = getUserAccount(authentication);
        Store store = validateStoreOwnership(storeId, userAccount);
        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);
        updateMenuDetails(menu, menuDTO, multipartFile);
        return StoreDTO.entityToDTO(store);
    }

    @Transactional
    public String deleteMenu(Authentication authentication, Long menuId, Long id){
        UserAccount userAccount = getUserAccount(authentication);
        Menu menu = menuRepository.findById(menuId).orElseThrow(MenuNotFoundException::new);
        if (!menu.getStore().getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("메뉴 삭제 권한이 없습니다.");
        }
        menuRepository.delete(menu);
        return "메뉴가 삭제되었습니다.";
    }

    private UserAccount getUserAccount(Authentication authentication) {
        String userId = authentication.getName();
        return userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("사용자 계정을 찾을 수 없습니다: " + userId));
    }

    private Store validateStoreOwnership(Long storeId, UserAccount userAccount) {
        Store store = storeRepository.findById(storeId).orElseThrow(StoreNotFoundException::new);
        if (!store.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("해당 가게의 권한이 없습니다.");
        }
        return store;
    }

    private void updateMenuDetails(Menu menu, MenuUpdateDTO menuDTO, MultipartFile multipartFile) {
        menu.setName(menuDTO.getName());
        menu.setPrice(menuDTO.getPrice());
        menu.setMenuDetail(menuDTO.getMenuDetail());
        menu.setStatus(menuDTO.getStatus()); // MenuStatus 업데이트
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

    public List<MenuDTO> findMenusByStoreId(Long storeId) {
        List<Menu> menus = menuRepository.findByStoreId(storeId);
        return menus.stream()
                .map(MenuDTO::entityToDTO)
                .collect(Collectors.toList());
    }

    public Object createMenuList(List<MenuListDTO> menuListDTO) {
        List<Menu> menus = menuListDTO.stream()
                .map(this::dtoToEntity)
                .toList();
        List<Menu> menusSave = menuRepository.saveAll(menus);

        return menusSave.stream()
                .map(MenuDTO::entityToDTO)
                .collect(Collectors.toList());
    }

    public Menu dtoToEntity(MenuListDTO menuListDTO) {
        Store store = storeRepository.findById(menuListDTO.getStoreId())
                .orElseThrow(StoreNotFoundException::new);
        return Menu.builder()
                .id(null)
                .store(store)
                .name(menuListDTO.getName())
                .imageUrl(menuListDTO.getImageUrl())
                .price(menuListDTO.getPrice())
                .menuDetail(menuListDTO.getMenuDetail())
                .status(MenuStatus.SALE)
                .build();
    }
}

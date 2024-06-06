package com.example.backend.controller.store;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.store.*;
import com.example.backend.entity.store.Category;
import com.example.backend.exception.store.StoreNotFoundException;
import com.example.backend.service.store.StoreService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/")
    public ResponseDTO<?> insert(Authentication authentication,
                                 @ModelAttribute StoreInsertDTO storeDTO,
                                 @RequestPart(value = "file") MultipartFile multipartFile) {
        try {
            // 사용자가 인증되어 있는지 확인합니다.
            if (authentication == null) {
                // 인증되지 않은 경우에 대한 처리
                return new ResponseDTO<>(HttpStatus.UNAUTHORIZED.value(), "인증되지 않은 사용자입니다.");
            }

            // 사용자의 인증 정보를 전달하여 가게를 등록합니다.
            StoreDTO insertedStore = storeService.insert(authentication, storeDTO, multipartFile);
            return new ResponseDTO<>(HttpStatus.OK.value(), insertedStore);
        } catch (IllegalStateException e) {
            // 가게 등록에 필요한 권한이 없는 경우 예외를 처리합니다.
            return new ResponseDTO<>(HttpStatus.FORBIDDEN.value(), e.getMessage());
        }
    }
    @ApiOperation(value = "가게 한건 조회", notes = "{id}를 통해 가게를 조회한다.")
    @GetMapping("/{id}")
    public ResponseDTO<?> select(@PathVariable Long id){
        return new ResponseDTO<>(HttpStatus.OK.value(), storeService.findById(id).orElseThrow(StoreNotFoundException::new));
    }

    @ApiOperation(value = "가게 여러건 조회", notes = "pageNumber=0&pageSize=10")
    @GetMapping("/")
    public ResponseDTO<?> selectAll(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)
                                    Pageable pageable){
        return new ResponseDTO<>(HttpStatus.OK.value(), storeService.selectAll(pageable));
    }

    @ApiOperation(value = "카테고리별 가게 조회")
    @GetMapping("/category/stores")
    public ResponseDTO<?> selectCategoryByCategory(@RequestParam("category") Category category) {
        CategoryDTO categoryDTO = CategoryDTO.builder()
                .category(category)
                .category_ko(category.label())
                .build();
        return new ResponseDTO<>(HttpStatus.OK.value(), storeService.selectCategory(categoryDTO));
    }


    @ApiOperation(value = "가게정보 수정", notes = "id, name, minOrderPrice, backgroundImageUrl, category, infor을 받는다.")
    @PutMapping("/{storeId}")
    public ResponseDTO<?> update(Authentication authentication,
                                 @PathVariable Long storeId,
                                 @RequestBody StoreInsertDTO storeDTO){
        return new ResponseDTO<>(HttpStatus.OK.value(), storeService.update(authentication, storeId, storeDTO));
    }

    @ApiOperation(value = "가게정보 삭제", notes = "storeId를 받아서 가게정보를 삭제한다.")
    @DeleteMapping("/")
    public ResponseDTO<?> delete(Authentication authentication, @RequestParam Long id){
        storeService.delete(authentication, id);
        return new ResponseDTO<>(HttpStatus.OK.value(), "가게가 삭제되었습니다.");
    }

    @ApiOperation(value = "카테고리 가져오기")
    @GetMapping("/category")
    public ResponseDTO<?> selectCategory(){
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for(Category c : Category.values()){
            categoryDTOS.add(CategoryDTO.builder()
                    .category(c)
                    .category_ko(c.label())
                    .build());
        }

        return new ResponseDTO<>(HttpStatus.OK.value(), categoryDTOS);
    }

    @ApiOperation(value = "Store 검색기능")
    @PostMapping("/search")
    public ResponseDTO<?> search(@RequestBody StoreSearchReqeustDTO ssrDTO){
        return new ResponseDTO<>(HttpStatus.OK.value(),
                storeService.search(ssrDTO));
    }
    @GetMapping("/my")
    public List<StoreOwnerDTO> getMyStores(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "인증이 필요합니다.");
        }
        return storeService.findStoresByOwner(authentication);
    }
}
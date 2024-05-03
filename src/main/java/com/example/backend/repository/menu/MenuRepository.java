package com.example.backend.repository.menu;

import com.example.backend.entity.menu.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Autowired
    RedisTemplate<String, Menu> redisTemplate = new RedisTemplate<>();

    @Query("select m from Menu m join fetch m.store s where m.id = :id")
    Optional<Menu> findByMenuIdWithStore(Long id);

    default Optional<Menu> findByMenuId(Long id) {
        // Redis에서 메뉴를 가져오는 로직
        Menu menu = redisTemplate.opsForValue().get(String.valueOf(id));
        if (menu != null) {
            return Optional.of(menu);
        } else {
            // Redis에 없는 경우, 데이터베이스에서 메뉴를 조회하여 Redis에 저장하고 반환
            Optional<Menu> optionalMenu = findById(id);
            optionalMenu.ifPresent(value -> redisTemplate.opsForValue().set(String.valueOf(value.getId()), value));
            return optionalMenu;
        }
    }
}

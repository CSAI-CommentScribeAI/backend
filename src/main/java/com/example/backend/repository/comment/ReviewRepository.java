package com.example.backend.repository.comment;

import com.example.backend.entity.comment.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByStoreId(Long store_id);

    List<Review> findByUserAccountId(Long userAccountId);

    Review findByOrderId(Long orderId);


    List<Review> findTop10ByUserAccountIdAndStoreIdOrderByCreateAtDesc(Long userId, Long storeId);
}

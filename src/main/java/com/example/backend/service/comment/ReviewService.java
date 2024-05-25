package com.example.backend.service.comment;

import com.example.backend.dto.comment.ReviewDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.entity.userAccount.UserRole;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.comment.ReviewRepository;
import com.example.backend.repository.order.OrderRepository;
import com.example.backend.repository.store.StoreRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final UserAccountRepository userAccountRepository;
    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public Review createReview(Authentication authentication, ReviewDTO reviewDTO, int storeId) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 사용자의 역할이 ROLE_USER가 아니라면 예외 발생
        if (userAccount.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalStateException("User does not have permission to register a review.");
        }

        // 상점과 주문 객체 조회
        Store store = storeRepository.findById((long) storeId)
                .orElseThrow(() -> new IllegalStateException("Store not found with ID: " + reviewDTO.getStoreId()));

        Order order = orderRepository.findById((long) reviewDTO.getOrderId())
                .orElseThrow(() -> new IllegalStateException("Order not found with ID: " + reviewDTO.getOrderId()));

        // Review 객체 생성 및 설정
        Review review = Review.builder()
                .comment(reviewDTO.getComment())
                .createAt(LocalDateTime.now())
                .userAccount(userAccount)
                .store(store)
                .order(order)
                .build();

        // Review 객체를 데이터베이스에 저장
        reviewRepository.save(review);

        return review;
    }

    public Review updateReview(Authentication authentication, int reviewId, ReviewDTO reviewDTO) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 기존 리뷰를 데이터베이스에서 조회
        Review review = reviewRepository.findById((long) reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + reviewId));

        // 사용자가 해당 리뷰의 작성자인지 확인
        if (!review.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("User does not have permission to update this review.");
        }

        // 리뷰 내용과 수정 시간을 업데이트
        review.setComment(reviewDTO.getComment());
        review.setUpdateAt(LocalDateTime.now());

        // Review 객체를 데이터베이스에 저장 (업데이트)
        reviewRepository.save(review);

        return review;
    }

    public void deleteReview(Authentication authentication, int reviewId) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 기존 리뷰를 데이터베이스에서 조회
        Review review = reviewRepository.findById((long) reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + reviewId));

        // 사용자가 해당 리뷰의 작성자인지 확인
        if (!review.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("User does not have permission to delete this review.");
        }

        // Review 객체를 데이터베이스에서 삭제
        reviewRepository.delete(review);
    }

    public List<Review> getReviewsByStoreId(int storeId) {
        return reviewRepository.findByStoreId((long) storeId);
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviewRepository.findByUserAccountId((long) userId);
    }

    public Review getReviewByOrderId(int orderId) {
        return reviewRepository.findByOrderId((long) orderId);
    }
}

package com.example.backend.service.comment;

import com.example.backend.dto.comment.ReplyRequestDTO;
import com.example.backend.dto.comment.ReplyResponseDTO;
import com.example.backend.dto.comment.ReviewDTO;
import com.example.backend.dto.comment.ReviewRequestDTO;
import com.example.backend.dto.userAccount.UserAccountRequestDTO;
import com.example.backend.entity.comment.Reply;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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

    public ReviewDTO createReview(Authentication authentication, ReviewRequestDTO reviewDTO, int orderId) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        if (userAccount.getUserRole() != UserRole.ROLE_USER) {
            throw new IllegalStateException("User does not have permission to register a review.");
        }

        Order order = orderRepository.findById((long) orderId)
                .orElseThrow(() -> new IllegalStateException("Order not found with ID: " + orderId));

        LocalDateTime createdTime = order.getCreatedTime();
        LocalDateTime now = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(createdTime, now);
        if (daysBetween > 3) {
            throw new IllegalStateException("Cannot register a review for an order older than 3 days.");
        }

        if (!Long.valueOf(userId).equals(order.getUserAccount().getId())) {
            throw new IllegalStateException("User does not have permission to register a review.");
        }

        Store store = storeRepository.findById(order.getStoreId())
                .orElseThrow(() -> new IllegalStateException("Store not found with ID: " + order.getStoreId()));

        Review review = Review.builder()
                .rating(reviewDTO.getRating())
                .comment(reviewDTO.getComment())
                .createAt(LocalDateTime.now())
                .userAccount(userAccount)
                .store(store)
                .order(order)
                .build();

        reviewRepository.save(review);

        return toReviewDTO(review);
    }

    public ReviewDTO updateReview(Authentication authentication, int orderId, ReviewDTO reviewDTO) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 기존 리뷰를 데이터베이스에서 조회
        Review review = reviewRepository.findById((long) orderId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + orderId));

        // 사용자가 해당 리뷰의 작성자인지 확인
        if (!review.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("User does not have permission to update this review.");
        }

        // 리뷰 내용과 수정 시간을 업데이트
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setUpdateAt(LocalDateTime.now());

        // Review 객체를 데이터베이스에 저장 (업데이트)
        reviewRepository.save(review);

        return toReviewDTO(review);
    }

    public void deleteReview(Authentication authentication, int orderId) {
        // 사용자의 ID 가져오기
        String userId = authentication.getName();

        // 사용자 계정을 데이터베이스에서 조회
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        // 기존 리뷰를 데이터베이스에서 조회
        Review review = reviewRepository.findById((long) orderId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + orderId));

        // 사용자가 해당 리뷰의 작성자인지 확인
        if (!review.getUserAccount().getId().equals(userAccount.getId())) {
            throw new IllegalStateException("User does not have permission to delete this review.");
        }

        // Review 객체를 데이터베이스에서 삭제
        reviewRepository.delete(review);
    }

    public List<ReviewDTO> getReviewsByStoreId(int storeId) {
        List<Review> reviews = reviewRepository.findByStoreId((long) storeId);
        return reviews.stream().map(this::toReviewDTO).collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByUserId(int userId) {
        List<Review> reviews = reviewRepository.findByUserAccountId((long) userId);
        return reviews.stream().map(this::toReviewDTO).collect(Collectors.toList());
    }

    public ReviewDTO getReviewByOrderId(int orderId) {
        Review review = reviewRepository.findByOrderId((long) orderId);
        return toReviewDTO(review);
    }

    private ReviewDTO toReviewDTO(Review review) {
        return ReviewDTO.builder()
                .orderId(review.getOrder().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userId(review.getUserAccount().getId())
                .storeId(review.getStore().getId())
                .replies(review.getReplies() == null ? new ArrayList<>() : review.getReplies().stream()
                        .map(this::toReplyDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    private UserAccountRequestDTO toUserAccountDTO(UserAccount userAccount) {
        return UserAccountRequestDTO.builder()
                .email(userAccount.getEmail())
                .nickname(userAccount.getNickname())
                .build();
    }

    private ReplyResponseDTO toReplyDTO(Reply reply) {
        return ReplyResponseDTO.builder()
                .id(reply.getId())
                .comment(reply.getComment())
                .createAt(reply.getCreateAt())
                .updateAt(reply.getUpdateAt())
                .deleteAt(reply.getDeleteAt())
                .build();
    }
}

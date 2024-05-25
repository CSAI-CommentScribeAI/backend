package com.example.backend.controller.comment;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.comment.ReviewDTO;
import com.example.backend.entity.comment.Review;
import com.example.backend.repository.comment.ReviewRepository;
import com.example.backend.service.comment.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{storeId}")
    public ResponseDTO<?> createReview(Authentication authentication, @RequestBody ReviewDTO reviewDTO, @PathVariable int storeId) {
        try {
            Review review = reviewService.createReview(authentication, reviewDTO, storeId);
            return ResponseDTO.builder()
                    .status(200)
                    .data(review)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @PutMapping("/{reviewId}")
    public ResponseDTO<?> updateReview(Authentication authentication, @PathVariable int reviewId,
                                       @RequestBody ReviewDTO reviewDTO) {
        try {
            Review review = reviewService.updateReview(authentication, reviewId, reviewDTO);
            return ResponseDTO.builder()
                    .status(200)
                    .data(review)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseDTO<?> deleteReview(Authentication authentication, @PathVariable int reviewId) {
        try {
            reviewService.deleteReview(authentication, reviewId);
            return ResponseDTO.builder()
                    .status(200)
                    .data("Review deleted successfully.")
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @GetMapping("/store/{storeId}")
    public ResponseDTO<?> getReviewByStoreId(@PathVariable int storeId) {
        List<Review> reviews = reviewService.getReviewsByStoreId(storeId);
        return ResponseDTO.builder()
                .status(200)
                .data(reviews)
                .build();
    }

    @GetMapping("/user/{userId}")
    public ResponseDTO<?> getReviewByUserId(@PathVariable int userId) {
        List<Review> reviews = reviewService.getReviewsByUserId(userId);
        return ResponseDTO.builder()
                .status(200)
                .data(reviews)
                .build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseDTO<?> getReviewByOrderId(@PathVariable int orderId) {
        Review review = reviewService.getReviewByOrderId(orderId);
        return ResponseDTO.builder()
                .status(200)
                .data(review)
                .build();
    }
}

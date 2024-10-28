package com.example.backend.service.comment;

import com.example.backend.dto.comment.ReplyRequestDTO;
import com.example.backend.dto.comment.ReplyResponseDTO;
import com.example.backend.dto.openAI.ChatGPTRequestDTO;
import com.example.backend.dto.openAI.ChatGPTResponseDTO;
import com.example.backend.entity.comment.Reply;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.openAI.ReplySave;
import com.example.backend.entity.order.UserOrder;
import com.example.backend.entity.order.OrderMenu;
import com.example.backend.entity.store.Store;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.comment.ReplyRepository;
import com.example.backend.repository.comment.ReviewRepository;
import com.example.backend.repository.openAI.ReplySaveRepository;
import com.example.backend.repository.order.OrderMenuRepository;
import com.example.backend.repository.store.StoreRepository;
import com.example.backend.service.order.OrderService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class ReplyImplService implements ReplyService{

    @Value("${openai.model}")
    private String model;

    @Value("${openai.apiurl}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    private final UserAccountRepository userAccountRepository;
    private final ReplyRepository replyRepository;
    private final ReplySaveRepository replySaveRepository;
    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final StoreRepository storeRepository;
    private final OrderMenuRepository orderMenuRepository;

    @Override
    public ReplyResponseDTO createReply(Authentication authentication, ReplyRequestDTO replyRequestDTO, int reviewId) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        Review review = reviewRepository.findById((long) reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + reviewId));

        Reply reply = new Reply();
        reply.setComment(replyRequestDTO.getComment());
        reply.setUserAccount(userAccount);
        reply.setReview(review);
        reply.setCreateAt(LocalDateTime.now());
        reply.setUserAccount(userAccount);
        reply.setDeleteAt(null);
        reply.setUpdateAt(null);
        Reply replySave = replyRepository.save(reply);

        return ReplyResponseDTO.builder()
                .id(replySave.getId())
                .comment(replySave.getComment())
                .createAt(replySave.getCreateAt())
                .updateAt(replySave.getUpdateAt())
                .deleteAt(replySave.getDeleteAt())
                .userId(replySave.getUserAccount().getId())
                .nickName(replySave.getUserAccount().getNickname())
                .reviewId(replySave.getReview().getId())
                .build();

    }

    @Override
    public ReplyResponseDTO updateReply(Authentication authentication, int replyId, ReplyRequestDTO replyRequestDTO) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        Reply reply = replyRepository.findById((long) replyId)
                .orElseThrow(() -> new IllegalStateException("Reply not found with ID: " + replyId));

        if(!reply.getUserAccount().getId().equals(userAccount.getId())){
            throw new IllegalStateException("User does not have permission to update a reply.");
        }

        reply.setComment(replyRequestDTO.getComment());
        reply.setUpdateAt(LocalDateTime.now());

        Reply replySave = replyRepository.save(reply);

        return ReplyResponseDTO.builder()
                .id(replySave.getId())
                .comment(replySave.getComment())
                .createAt(replySave.getCreateAt())
                .updateAt(replySave.getUpdateAt())
                .deleteAt(replySave.getDeleteAt())
                .userId(replySave.getUserAccount().getId())
                .nickName(replySave.getUserAccount().getNickname())
                .reviewId(replySave.getReview().getId())
                .build();
    }

    @Override
    public void deleteReply(Authentication authentication, int replyId) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        Reply reply = replyRepository.findById((long) replyId)
                .orElseThrow(() -> new IllegalStateException("Reply not found with ID: " + replyId));

        if(!reply.getUserAccount().getId().equals(userAccount.getId())){
            throw new IllegalStateException("User does not have permission to delete a reply.");
        }

        replyRepository.delete(reply);

    }

    @Override
    public List<ReplyResponseDTO> getReplyListByStoreId(int storeId) {
        List<Reply> replyList = replyRepository.findByReviewStoreId((long) storeId);

        return replyList.stream()
                .map(reply -> ReplyResponseDTO.builder()
                        .id(reply.getId())
                        .comment(reply.getComment())
                        .createAt(reply.getCreateAt())
                        .updateAt(reply.getUpdateAt())
                        .deleteAt(reply.getDeleteAt())
                        .userId(reply.getUserAccount().getId())
                        .nickName(reply.getUserAccount().getNickname())
                        .reviewId(reply.getReview().getId())
                        .build())
                .toList();
    }

    @Override
    public List<ReplyResponseDTO> getReplyList(int userId) {
        List<Reply> replyList = replyRepository.findByUserAccountId((long) userId);

        return replyList.stream()
                .map(reply -> ReplyResponseDTO.builder()
                        .id(reply.getId())
                        .comment(reply.getComment())
                        .createAt(reply.getCreateAt())
                        .updateAt(reply.getUpdateAt())
                        .deleteAt(reply.getDeleteAt())
                        .userId(reply.getUserAccount().getId())
                        .nickName(reply.getUserAccount().getNickname())
                        .reviewId(reply.getReview().getId())
                        .build())
                .toList();
    }

    @Override
    public String createReplyByAI(Authentication authentication, int reviewId) {
        Review review = reviewRepository.findById((long) reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + reviewId));

        UserAccount userAccount = userAccountRepository.findById(review.getUserAccount().getId())
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + review.getUserAccount().getId()));

        UserOrder userOrder = review.getUserOrder();

        Store store = storeRepository.findById(userOrder.getStoreId())
                .orElseThrow(() -> new IllegalStateException("상점 정보를 찾을 수 없습니다."));

        List<Review> reviews = orderService.getRecentReviewsForUserAndStore(userOrder.getUserAccount().getId(), userOrder.getStoreId());

        // 현재 리뷰 내용과 같은 리뷰는 제외
        List<Review> filteredReviews = reviews.stream()
                .filter(r -> !r.getComment().equals(review.getComment()))
                .collect(Collectors.toList());

        String reviewSection = filteredReviews.isEmpty() ?
                "리뷰 없음" :
                IntStream.range(0, filteredReviews.size())
                        .mapToObj(i -> (i + 1) + ". " + filteredReviews.get(i).getComment())
                        .collect(Collectors.joining("\n"));

        List<OrderMenu> orderDetails = orderMenuRepository.findByOrderId(userOrder.getId());
        if (orderDetails == null || orderDetails.isEmpty()) {
            throw new IllegalStateException("Order menus not found for order ID " + userOrder.getId());
        }

        String orderDetailsString = orderDetails.stream()
                .map(orderMenu -> "- " + orderMenu.getMenuName() + " x " + orderMenu.getQuantity())
                .collect(Collectors.joining("\n"));

        String prompt = String.format("너는 지금 부터 배달앱에 리뷰에 답글을 작성하는 사장님이야\n" +
                        "내가 가게 이름과 주문한 고객의 이름, 주문내역, 리뷰내용, 최근리뷰내용을 줄거야 이거에 맞게 너가 리뷰 답글을 작성해\n\n" +
                        "- 가게이름 : %s\n" +
                        "- 고객닉네임 : %s\n" +
                        "- 주문내역:\n    %s\n" +
                        "- 별점 : %f\n" +
                        "- 리뷰내용 :\n    %s\n" +
                        "- 최근리뷰내용 :\n    %s\n\n" +
                        "- 친근하게 작성해\n" +
                        "- 이모티콘을 사용해서 작성해\n" +
                        "- 최근리뷰내용과 리뷰내용을 종합하여 작성해\n" +
                        "- 별점을 기준으로 내용을 작성해",
                store.getName(),
                userAccount.getNickname(),
                orderDetailsString,
                review.getRating(),
                review.getComment(),
                reviewSection);

        ChatGPTRequestDTO request = new ChatGPTRequestDTO(model, prompt);

        ChatGPTResponseDTO chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponseDTO.class);

        String reply = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        ReplySave replySave = new ReplySave();
        replySave.setReview(review);
        replySave.setReplyContent(reply);
        replySave.setStore(store);


        replySaveRepository.save(replySave);

        return reply;
    }
}

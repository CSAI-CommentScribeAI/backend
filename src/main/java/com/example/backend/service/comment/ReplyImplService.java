package com.example.backend.service.comment;

import com.example.backend.dto.comment.ReplyDTO;
import com.example.backend.dto.openAI.ChatGPTRequestDTO;
import com.example.backend.dto.openAI.ChatGPTResponseDTO;
import com.example.backend.dto.openAI.ReplySaveDTO;
import com.example.backend.entity.comment.Reply;
import com.example.backend.entity.comment.Review;
import com.example.backend.entity.openAI.ReplySave;
import com.example.backend.entity.order.Order;
import com.example.backend.entity.userAccount.UserAccount;
import com.example.backend.repository.UserAccount.UserAccountRepository;
import com.example.backend.repository.comment.ReplyRepository;
import com.example.backend.repository.comment.ReviewRepository;
import com.example.backend.repository.openAI.ReplySaveRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)
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

    @Override
    public ReplyDTO createReply(Authentication authentication, ReplyDTO replyDTO, int reviewId) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        Review review = reviewRepository.findById((long) reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + reviewId));

        Reply reply = new Reply();
        reply.setComment(replyDTO.getComment());
        reply.setUserAccount(userAccount);
        reply.setReview(review);
        reply.setCreateAt(LocalDateTime.now());

        Reply replySave = replyRepository.save(reply);

        return ReplyDTO.builder()
                .id(replySave.getId())
                .comment(replySave.getComment())
                .createAt(replySave.getCreateAt())
                .updateAt(replySave.getUpdateAt())
                .deleteAt(replySave.getDeleteAt())
                .userId(replySave.getUserAccount().getId())
                .reviewId(replySave.getReview().getId())
                .build();

    }

    @Override
    public ReplyDTO updateReply(Authentication authentication, int replyId, ReplyDTO replyDTO) {
        String userId = authentication.getName();
        UserAccount userAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        Reply reply = replyRepository.findById((long) replyId)
                .orElseThrow(() -> new IllegalStateException("Reply not found with ID: " + replyId));

        if(!reply.getUserAccount().getId().equals(userAccount.getId())){
            throw new IllegalStateException("User does not have permission to update a reply.");
        }

        reply.setComment(replyDTO.getComment());
        reply.setUpdateAt(LocalDateTime.now());

        Reply replySave = replyRepository.save(reply);

        return ReplyDTO.builder()
                .id(replySave.getId())
                .comment(replySave.getComment())
                .createAt(replySave.getCreateAt())
                .updateAt(replySave.getUpdateAt())
                .deleteAt(replySave.getDeleteAt())
                .userId(replySave.getUserAccount().getId())
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
    public List<ReplyDTO> getReplyListByStoreId(int storeId) {
        List<Reply> replyList = replyRepository.findByReviewStoreId((long) storeId);

        return replyList.stream()
                .map(reply -> ReplyDTO.builder()
                        .id(reply.getId())
                        .comment(reply.getComment())
                        .createAt(reply.getCreateAt())
                        .updateAt(reply.getUpdateAt())
                        .deleteAt(reply.getDeleteAt())
                        .userId(reply.getUserAccount().getId())
                        .reviewId(reply.getReview().getId())
                        .build())
                .toList();
    }

    @Override
    public List<ReplyDTO> getReplyList(int userId) {
        List<Reply> replyList = replyRepository.findByUserAccountId((long) userId);

        return replyList.stream()
                .map(reply -> ReplyDTO.builder()
                        .id(reply.getId())
                        .comment(reply.getComment())
                        .createAt(reply.getCreateAt())
                        .updateAt(reply.getUpdateAt())
                        .deleteAt(reply.getDeleteAt())
                        .userId(reply.getUserAccount().getId())
                        .reviewId(reply.getReview().getId())
                        .build())
                .toList();
    }

    @Override
    public String createReplyByAI(Authentication authentication, int reviewId) {
        Review review = reviewRepository.findById((long) reviewId)
                .orElseThrow(() -> new IllegalStateException("Review not found with ID: " + reviewId));

        String userId = authentication.getName();

        UserAccount ownerAccount = userAccountRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + userId));

        if(ownerAccount.getUserRole().equals("ROLE_OWNER")){
            throw new IllegalStateException("User does not have permission to create a reply.");
        }

        UserAccount userAccount = userAccountRepository.findById(review.getUserAccount().getId())
                .orElseThrow(() -> new IllegalStateException("User not found with ID: " + review.getUserAccount().getId()));

        Order order = review.getOrder();

        String prompt = "리뷰 내용: " + review.getComment();

        ChatGPTRequestDTO request = new ChatGPTRequestDTO(model, prompt);

        ChatGPTResponseDTO chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponseDTO.class);

        String reply = chatGPTResponse.getChoices().get(0).getMessage().getContent();

        ReplySave replySave = new ReplySave();
        replySave.setReview(review);
        replySave.setReplyContent(reply);
        replySave.setStore(review.getStore());

        replySaveRepository.save(replySave);

        return reply;
    }
}

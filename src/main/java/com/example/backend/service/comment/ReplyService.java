package com.example.backend.service.comment;

import com.example.backend.dto.comment.ReplyRequestDTO;
import com.example.backend.dto.comment.ReplyResponseDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface ReplyService {
    ReplyResponseDTO createReply(Authentication authentication, ReplyRequestDTO replyRequestDTO, int reviewId);
    
    ReplyResponseDTO updateReply(Authentication authentication, int replyId, ReplyRequestDTO replyRequestDTO);
    
    void deleteReply(Authentication authentication, int replyId);

    List<ReplyResponseDTO> getReplyListByStoreId(int storeId);

    List<ReplyResponseDTO> getReplyList(int userId);

    String createReplyByAI(Authentication authentication, int reviewId);
}

package com.example.backend.service.comment;

import com.example.backend.dto.comment.ReplyDTO;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface ReplyService {
    ReplyDTO createReply(Authentication authentication, ReplyDTO replyDTO, int reviewId);
    
    ReplyDTO updateReply(Authentication authentication, int replyId, ReplyDTO replyDTO);
    
    void deleteReply(Authentication authentication, int replyId);

    List<ReplyDTO> getReplyListByStoreId(int storeId);

    List<ReplyDTO> getReplyList(int userId);

    String createReplyByAI(Authentication authentication, int reviewId);
}

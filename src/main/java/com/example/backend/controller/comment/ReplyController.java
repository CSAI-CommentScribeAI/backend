package com.example.backend.controller.comment;

import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.comment.ReplyDTO;
import com.example.backend.service.comment.ReplyService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment/reply")
@RequiredArgsConstructor
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{reviewId}")
    public ResponseDTO<?> createReply(Authentication authentication, @RequestBody ReplyDTO replyDTO, @PathVariable int reviewId) {
        try {
            ReplyDTO reply = replyService.createReply(authentication, replyDTO, reviewId);
            return ResponseDTO.builder()
                    .status(200)
                    .data(reply)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @PutMapping("/{replyId}")
    public ResponseDTO<?> updateReply(Authentication authentication, @PathVariable int replyId,
                                       @RequestBody ReplyDTO replyDTO) {
        try {
            ReplyDTO reply = replyService.updateReply(authentication, replyId, replyDTO);
            return ResponseDTO.builder()
                    .status(200)
                    .data(reply)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @DeleteMapping("/{replyId}")
    public ResponseDTO<?> deleteReply(Authentication authentication, @PathVariable int replyId) {
        try {
            replyService.deleteReply(authentication, replyId);
            return ResponseDTO.builder()
                    .status(200)
                    .data("Deleted")
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @GetMapping("/{userId}")
    public ResponseDTO<?> getReplyList(@PathVariable int userId) {
        try {
            List<ReplyDTO> replyList = replyService.getReplyList(userId);
            return ResponseDTO.builder()
                    .status(200)
                    .data(replyList)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @GetMapping("/{storeId}")
    public ResponseDTO<?> getReplyListByStoreId(@PathVariable int storeId) {
        try {
            List<ReplyDTO> replyList = replyService.getReplyListByStoreId(storeId);
            return ResponseDTO.builder()
                    .status(200)
                    .data(replyList)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

    @PostMapping("/ai/{reviewId}")
public ResponseDTO<?> createReplyByAI(Authentication authentication, @PathVariable int reviewId) {
        try {
            String reply = replyService.createReplyByAI(authentication, reviewId);
            return ResponseDTO.builder()
                    .status(200)
                    .data(reply)
                    .build();
        } catch (Exception e) {
            String error = e.getMessage();
            return ResponseDTO.builder()
                    .status(400)
                    .data(error)
                    .build();
        }
    }

}

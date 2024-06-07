package com.example.backend.repository.comment;

import com.example.backend.entity.comment.Reply;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findByReviewStoreId(Long storeId);

    List<Reply> findByUserAccountId(long userId);
}

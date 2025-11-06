package com.example.bbs.repository;

import com.example.bbs.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comments, Long> {

    // 루트 댓글만 (parentComment == null)
    List<Comments> findByPostIdAndParentCommentIsNullOrderByCreatedAtAsc(Long postId);

    // 특정 댓글의 자식 댓글 조회
    List<Comments> findByParentCommentIdOrderByCreatedAtAsc(Long parentId);
}
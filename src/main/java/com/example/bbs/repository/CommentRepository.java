package com.example.bbs.repository;

import com.example.bbs.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 루트 댓글만 (parentComment == null)
    List<Comment> findByPostIdAndParentCommentIsNullOrderByCreatedAtAsc(Long postId);

    // 특정 댓글의 자식 댓글 조회
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentId);
}
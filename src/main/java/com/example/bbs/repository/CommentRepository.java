package com.example.bbs.repository;

import com.example.bbs.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 루트 댓글만 (parentComment == null)
    List<Comment> findByPostIdAndParentCommentIsNullAndHiddenFalseOrderByCreatedAtAsc(Long postId);

    // 특정 댓글의 자식 댓글 조회
    //List<Comment> findByParentCommentIdAndHiddenFalseOrderByCreatedAtAsc(Long parentId);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentId AND c.hidden = false ORDER BY c.createdAt ASC")
    List<Comment> findVisibleReplies(@Param("parentId") Long parentId);
}
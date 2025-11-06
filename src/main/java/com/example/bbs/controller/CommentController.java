package com.example.bbs.controller;

import com.example.bbs.entity.Comment;
import com.example.bbs.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // ✅ 루트 댓글 목록
    @GetMapping
    public List<Comment> getRootComments(@PathVariable Long postId) {
        return commentService.getRootComments(postId);
    }

    // ✅ 특정 댓글의 대댓글 목록
    @GetMapping("/{parentId}/replies")
    public List<Comment> getReplies(@PathVariable Long parentId) {
        return commentService.getReplies(parentId);
    }

    // ✅ 댓글 작성 (루트)
    @PostMapping
    public ResponseEntity<Comment> addComment(@PathVariable Long postId,
                                              @RequestBody Comment comment,
                                              HttpServletRequest req) {
        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For"))
                .orElse(req.getRemoteAddr());
        comment.setIpAddress(ip);
        return ResponseEntity.ok(commentService.addComment(postId, comment));
    }

    // ✅ 대댓글 작성
    @PostMapping("/{parentId}/reply")
    public ResponseEntity<Comment> addReply(@PathVariable Long postId,
                                            @PathVariable Long parentId,
                                            @RequestBody Comment reply,
                                            HttpServletRequest req) {
        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For"))
                .orElse(req.getRemoteAddr());
        reply.setIpAddress(ip);
        return ResponseEntity.ok(commentService.addReply(postId, parentId, reply));
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

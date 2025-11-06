package com.example.bbs.controller;

import com.example.bbs.dto.CommentRequest;
import com.example.bbs.dto.CommentResponse;
import com.example.bbs.entity.Comment;
import com.example.bbs.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public List<CommentResponse> getRootComments(@PathVariable Long postId) {
        return commentService.getRootComments(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // ✅ 특정 댓글의 대댓글 목록
    @GetMapping("/{parentId}/replies")
    public List<Comment> getReplies(@PathVariable Long parentId) {
        return commentService.getReplies(parentId);
    }

    // ✅ 댓글 작성 (루트)
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequest request,
            HttpServletRequest req) {

        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For"))
                .orElse(req.getRemoteAddr());

        Comment saved = commentService.addComment(postId, request.getAuthor(), request.getContent(), ip);
        return ResponseEntity.ok(CommentResponse.from(saved));
    }

    // ✅ 대댓글 작성
    @PostMapping("/{parentId}/replies")
    public ResponseEntity<CommentResponse> addReply(@PathVariable Long postId,
                                                    @PathVariable Long parentId,
                                                    @Valid @RequestBody CommentRequest request,
                                                    HttpServletRequest req) {
        String ip = Optional.ofNullable(req.getHeader("X-Forwarded-For"))
                .orElse(req.getRemoteAddr());

        Comment saved = commentService.addReply(postId, parentId,
                request.getAuthor(),
                request.getContent(),
                ip);

        return ResponseEntity.ok(CommentResponse.from(saved));
    }

    // ✅ 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}

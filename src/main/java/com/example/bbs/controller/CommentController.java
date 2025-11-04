package com.example.bbs.controller;

import com.example.bbs.entity.Comment;
import com.example.bbs.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/post/{postId}")
    public List<Comment> getByPost(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @PostMapping("/post/{postId}")
    public Comment create(@PathVariable Long postId, @RequestBody Comment comment) {
        return commentService.addComment(postId, comment);
    }

    @PatchMapping("/{id}/hide")
    public String hide(@PathVariable Long id) {
        return commentService.hideComment(id)
                ? "✅ 댓글이 삭제되었습니다."
                : "❌ 댓글을 찾을 수 없습니다.";
    }
}

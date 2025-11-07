package com.example.bbs.dto;

import com.example.bbs.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CommentResponse {

    private Long id;
    private String author;
    private String content;
    private LocalDateTime createdAt;
    private String ipAddress;
    private List<CommentResponse> replies; // 대댓글 포함

    public CommentResponse(Long id, String author, String content,
                           LocalDateTime createdAt, String ipAddress,
                           List<CommentResponse> replies) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.ipAddress = ipAddress;
        this.replies = replies;
    }

    // ✅ Comment → CommentResponse 변환 (재귀 포함)
    public static CommentResponse from(Comment comment) {
        boolean hidden = comment.isHidden();
        return new CommentResponse(
                comment.getId(),
                hidden ? "익명" : comment.getAuthor(),
                hidden ? "(삭제된 댓글입니다)" : comment.getContent(),
                comment.getCreatedAt(),
                hidden ? null : comment.getIpAddress(),
                comment.getReplies() != null
                        ? comment.getReplies().stream()
                        .filter(c -> !c.isHidden())
                        .map(CommentResponse::from)
                        .collect(Collectors.toList())
                        : List.of()
        );
    }

    // Getter
    public Long getId() { return id; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getIpAddress() { return ipAddress; }
    public List<CommentResponse> getReplies() { return replies; }
}

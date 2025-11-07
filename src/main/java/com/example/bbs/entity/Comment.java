package com.example.bbs.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private boolean hidden = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;  // ✅ 어떤 게시글에 속한 댓글인지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;  // ✅ null이면 루트 댓글

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.PERSIST)
    private List<Comment> replies = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(length = 45)
    private String ipAddress;

    public Comment() {}

    // 일반 댓글 생성자
    public Comment(Post post, String author, String content, String ipAddress) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.ipAddress = ipAddress;
    }

    // 대댓글 생성자
    public Comment(Post post, Comment parent, String author, String content, String ipAddress) {
        this.post = post;
        this.parentComment = parent;
        this.author = author;
        this.content = content;
        this.ipAddress = ipAddress;
    }

    // Getter/Setter
    public Long getId() { return id; }
    public Post getPost() { return post; }
    public Comment getParentComment() { return parentComment; }
    public List<Comment> getReplies() { return replies; }
    public String getAuthor() { return author; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getIpAddress() { return ipAddress; }
    public boolean isHidden() { return hidden; }

    public void setPost(Post post) { this.post = post; }
    public void setParentComment(Comment parent) { this.parentComment = parent; }
    public void setAuthor(String author) { this.author = author; }
    public void setContent(String content) { this.content = content; }
    public void setIpAddress(String ip) { this.ipAddress = ip; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
}
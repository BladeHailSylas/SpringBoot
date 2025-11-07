package com.example.bbs.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originalName; // 사용자가 업로드한 파일명

    @Column(nullable = false)
    private String storedName;   // 서버에 저장된 실제 파일명

    @Column(nullable = false)
    private String filePath;     // 파일 경로

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private boolean thumbnail = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    // 기본 생성자
    public Media() {}

    // 생성자
    public Media(Post post, String originalName, String storedName,
                 String filePath, long size, String description, boolean thumbnail) {
        this.post = post;
        this.originalName = originalName;
        this.storedName = storedName;
        this.filePath = filePath;
        this.size = size;
        this.description = description;
        this.thumbnail = thumbnail;
    }


    // Getter / Setter
    public Long getId() { return id; }
    public String getOriginalName() { return originalName; }
    public String getStoredName() { return storedName; }
    public String getFilePath() { return filePath; }
    public long getSize() { return size; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public Post getPost() { return post; }
    public String getDescription() { return description; }
    public boolean isThumbnail() { return thumbnail; }

    public void setPost(Post post) { this.post = post; }
    public void setOriginalName(String originalName) { this.originalName = originalName; }
    public void setStoredName(String storedName) { this.storedName = storedName; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public void setSize(long size) { this.size = size; }
}

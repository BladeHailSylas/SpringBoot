package com.example.bbs.dto;

import com.example.bbs.entity.Media;
import java.time.LocalDateTime;

public class MediaResponse {

    private Long id;
    private String originalName;
    private String url;
    private long size;
    private String description;
    private boolean thumbnail;
    private LocalDateTime uploadedAt;

    public MediaResponse(Media media) {
        this.id = media.getId();
        this.originalName = media.getOriginalName();
        this.size = media.getSize();
        this.uploadedAt = media.getUploadedAt();
        this.description = media.getDescription();
        this.thumbnail = media.isThumbnail();

        // 경로를 URL 형식으로 변환
        String normalizedPath = media.getFilePath().replace("\\", "/");
        this.url = "/" + normalizedPath;
    }

    // Getter
    public Long getId() { return id; }
    public String getOriginalName() { return originalName; }
    public String getUrl() { return url; }
    public long getSize() { return size; }
    public String getDescription() { return description; }
    public boolean isThumbnail() { return thumbnail; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
}

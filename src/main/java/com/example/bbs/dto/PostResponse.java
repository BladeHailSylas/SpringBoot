package com.example.bbs.dto;

import com.example.bbs.entity.Post;
import com.example.bbs.entity.Media;
import java.time.LocalDateTime;
import java.util.List;

public class PostResponse {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime createdAt;
    private String thumbnailUrl; // ✅ 썸네일용 URL
    private String content;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();
        this.content = post.getContent();
        // ✅ 첫 번째 미디어를 썸네일로 간주
        String thumbnail = null;
        try {
            java.lang.reflect.Field mediaField = post.getClass().getDeclaredField("medias");
            mediaField.setAccessible(true);
            Object mediaObj = mediaField.get(post);

            if (mediaObj instanceof List<?>) {
                List<?> list = (List<?>) mediaObj;
                if (!list.isEmpty() && list.get(0) instanceof Media media) {
                    thumbnail = "/" + media.getFilePath().replace("\\", "/");
                }
            }
        } catch (Exception ignored) {}

        this.thumbnailUrl = thumbnail;
    }

    // Getter
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getContent() { return content; }
}

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

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getAuthor();
        this.createdAt = post.getCreatedAt();

        // ✅ 첫 번째 미디어를 썸네일로 간주
        String thumbnail = null;
        try {
            // Media 리스트가 Post 엔티티에 매핑되어 있어야 합니다.
            // (없다면 Post에 @OneToMany(mappedBy="post") private List<Media> mediaList; 추가)
            java.lang.reflect.Field mediaField = post.getClass().getDeclaredField("mediaList");
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
}

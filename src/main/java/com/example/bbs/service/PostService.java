package com.example.bbs.service;

import com.example.bbs.entity.Post;
import com.example.bbs.entity.Media;
import com.example.bbs.repository.PostRepository;
import com.example.bbs.repository.MediaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final MediaRepository mediaRepository;

    public PostService(PostRepository postRepository, MediaRepository mediaRepository) {
        this.postRepository = postRepository;
        this.mediaRepository = mediaRepository;
    }

    public Page<Post> getAllVisiblePosts(Pageable pageable) {
        return postRepository.findByHiddenFalseOrderByIdDesc(pageable);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * 게시글 등록
     * 1️⃣ Post 저장
     * 2️⃣ 본문에 포함된 temp 이미지 이동 및 DB 등록
     */
    @Transactional
    public Post createPost(Post post) {
        Post saved = postRepository.save(post);
        try {
            moveTempImagesToPost(saved);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return saved;
    }

    public Optional<Post> updatePost(Long id, Post updated) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(updated.getTitle());
            post.setContent(updated.getContent());
            post.setAuthor(updated.getAuthor());
            return postRepository.save(post);
        });
    }

    public boolean hidePost(Long id) {
        return postRepository.findById(id).map(post -> {
            post.setHidden(true);
            postRepository.save(post);
            return true;
        }).orElse(false);
    }

    /**
     * 본문 내 temp 이미지를 찾아 실제 게시글 폴더로 이동하고 Media 등록
     */
    private void moveTempImagesToPost(Post post) throws IOException {
        if (post.getContent() == null) return;

        // 🔍 Markdown 내의 이미지 URL을 정규식으로 추출
        Pattern pattern = Pattern.compile("!\\[[^]]*]\\((/uploads/temp/[^)]+)\\)");
        Matcher matcher = pattern.matcher(post.getContent());

        Path targetDir = Path.of("uploads", String.valueOf(post.getId()));
        Files.createDirectories(targetDir);

        while (matcher.find()) {
            String tempPathStr = matcher.group(1); // ex) /uploads/temp/uuid_filename.png
            Path tempFile = Path.of("." + tempPathStr).normalize();

            if (Files.exists(tempFile)) {
                Path moved = targetDir.resolve(tempFile.getFileName());
                Files.move(tempFile, moved, StandardCopyOption.REPLACE_EXISTING);

                // ✅ Media 엔티티 DB 저장
                Media media = new Media();
                media.setPost(post);
                media.setOriginalName(tempFile.getFileName().toString());
                media.setStoredName(tempFile.getFileName().toString());
                media.setFilePath(moved.toString());
                media.setSize(Files.size(moved));
                mediaRepository.save(media);
                post.getMedias().add(media);
                // ✅ 게시글 본문 내 경로 수정
                String newPath = "/uploads/" + post.getId() + "/" + tempFile.getFileName();
                post.setContent(post.getContent().replace(tempPathStr, newPath));
            }
        }

        // 게시글 본문 경로 수정된 버전으로 다시 저장
        postRepository.save(post);
    }
}

package com.example.bbs.service;

import com.example.bbs.entity.Post;
import com.example.bbs.entity.Media;
import com.example.bbs.repository.PostRepository;
import com.example.bbs.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;
    private final PostRepository postRepository;

    public MediaService(MediaRepository mediaRepository, PostRepository postRepository) {
        this.mediaRepository = mediaRepository;
        this.postRepository = postRepository;
    }

    public Media saveFile(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String uploadDir = "uploads/" + postId;
        Files.createDirectories(Path.of(uploadDir));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, storedName);
        java.nio.file.Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Media entity = new Media();
        entity.setPost(post);
        entity.setOriginalName(file.getOriginalFilename());
        entity.setStoredName(storedName);
        entity.setFilePath(filePath.toString());
        entity.setSize(file.getSize());
        return mediaRepository.save(entity);
    }

    public List<Media> getFilesByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return mediaRepository.findByPost(post);
    }

    public void deleteFile(Long fileId) throws IOException {
        Media file = mediaRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
        java.nio.file.Files.deleteIfExists(Path.of(file.getFilePath()));
        mediaRepository.delete(file);
    }
}

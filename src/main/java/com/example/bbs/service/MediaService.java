package com.example.bbs.service;

import com.example.bbs.dto.MediaRequest;
import com.example.bbs.entity.Post;
import com.example.bbs.entity.Media;
import com.example.bbs.repository.PostRepository;
import com.example.bbs.repository.MediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * 파일 저장 및 DB 기록
     */
    @Transactional
    public Media saveFile(Long postId, MediaRequest request) throws IOException {
        MultipartFile file = request.getFile();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String uploadDir = "uploads/" + postId;
        Files.createDirectories(Path.of(uploadDir));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, storedName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Media media = new Media(
                post,
                file.getOriginalFilename(),
                storedName,
                filePath.toString(),
                file.getSize(),
                request.getDescription(),
                request.isThumbnail()
        );
        return mediaRepository.save(media);
    }

    @Transactional
    public Media saveTempFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/temp";
        Files.createDirectories(Path.of(uploadDir));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, storedName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        Media media = new Media();
        media.setOriginalName(file.getOriginalFilename());
        media.setStoredName(storedName);
        media.setFilePath(filePath.toString());
        media.setSize(file.getSize());
        return media; // DB에 저장 안 함 (임시)
    }

    /**
     * 게시글에 연결된 미디어 목록 조회
     */
    @Transactional(readOnly = true)
    public List<Media> getFilesByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return mediaRepository.findByPost(post);
    }

    /**
     * 파일 삭제 및 DB 정리
     */
    @Transactional
    public void deleteFile(Long fileId) throws IOException {
        Media file = mediaRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));

        // 파일 존재 시 삭제
        Path path = Path.of(file.getFilePath());
        Files.deleteIfExists(path);

        // DB에서도 삭제
        mediaRepository.delete(file);
    }
}

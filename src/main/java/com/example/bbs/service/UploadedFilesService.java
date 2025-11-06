package com.example.bbs.service;

import com.example.bbs.entity.Post;
import com.example.bbs.entity.UploadedFiles;
import com.example.bbs.repository.PostRepository;
import com.example.bbs.repository.UploadedFilesRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class UploadedFilesService {

    private final UploadedFilesRepository uploadedFilesRepository;
    private final PostRepository postRepository;

    public UploadedFilesService(UploadedFilesRepository uploadedFilesRepository, PostRepository postRepository) {
        this.uploadedFilesRepository = uploadedFilesRepository;
        this.postRepository = postRepository;
    }

    public UploadedFiles saveFile(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String uploadDir = "uploads/" + postId;
        Files.createDirectories(Path.of(uploadDir));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, storedName);
        java.nio.file.Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        UploadedFiles entity = new UploadedFiles();
        entity.setPost(post);
        entity.setOriginalName(file.getOriginalFilename());
        entity.setStoredName(storedName);
        entity.setFilePath(filePath.toString());
        entity.setSize(file.getSize());
        return uploadedFilesRepository.save(entity);
    }

    public List<UploadedFiles> getFilesByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return uploadedFilesRepository.findByPost(post);
    }

    public void deleteFile(Long fileId) throws IOException {
        UploadedFiles file = uploadedFilesRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
        java.nio.file.Files.deleteIfExists(Path.of(file.getFilePath()));
        uploadedFilesRepository.delete(file);
    }
}

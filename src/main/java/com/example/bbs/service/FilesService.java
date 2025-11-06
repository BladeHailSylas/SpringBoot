package com.example.bbs.service;

import com.example.bbs.entity.UploadedFiles;
import com.example.bbs.entity.Post;
import com.example.bbs.repository.FilesRepository;
import com.example.bbs.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class FilesService {

    private final FilesRepository filesRepository;
    private final PostRepository postRepository;

    public FilesService(FilesRepository filesRepository, PostRepository postRepository) {
        this.filesRepository = filesRepository;
        this.postRepository = postRepository;
    }

    public UploadedFiles saveFile(Long postId, MultipartFile file) throws IOException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // 업로드 디렉터리 설정
        String uploadDir = "uploads/" + postId;
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        // 파일명 처리
        String originalName = file.getOriginalFilename();
        String storedName = UUID.randomUUID() + "_" + originalName;
        Path filePath = Paths.get(uploadDir, storedName);

        // 파일 복사
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // DB 저장
        UploadedFiles entity = new UploadedFiles(post, originalName, storedName, filePath.toString(), file.getSize());
        return filesRepository.save(entity);
    }

    public List<UploadedFiles> getFilesByPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return filesRepository.findByPost(post);
    }

    public void deleteFile(Long fileId) throws IOException {
        UploadedFiles file = filesRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("파일을 찾을 수 없습니다."));
        Files.deleteIfExists(Path.of(file.getFilePath()));
        filesRepository.delete(file);
    }
}

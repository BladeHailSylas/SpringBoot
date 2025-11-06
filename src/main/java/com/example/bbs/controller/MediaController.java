package com.example.bbs.controller;

import com.example.bbs.entity.Media;
import com.example.bbs.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadMedia(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = "uploads/media";
        Files.createDirectories(Path.of(uploadDir));

        String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Path.of(uploadDir, storedName);
        java.nio.file.Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "/uploads/media/" + storedName; // 정적 리소스 경로
        return ResponseEntity.ok(fileUrl);
    }

    @GetMapping
    public List<Media> getFiles(@PathVariable Long postId) {
        return mediaService.getFilesByPost(postId);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        mediaService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

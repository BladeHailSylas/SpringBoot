package com.example.bbs.controller;

import com.example.bbs.entity.UploadedFiles;
import com.example.bbs.service.UploadedFilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/files")
public class UploadedFilesController {

    private final UploadedFilesService uploadedFilesService;

    public UploadedFilesController(UploadedFilesService uploadedFilesService) {
        this.uploadedFilesService = uploadedFilesService;
    }

    @PostMapping
    public ResponseEntity<UploadedFiles> uploadFile(@PathVariable Long postId,
                                                    @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(uploadedFilesService.saveFile(postId, file));
    }

    @GetMapping
    public List<UploadedFiles> getFiles(@PathVariable Long postId) {
        return uploadedFilesService.getFilesByPost(postId);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        uploadedFilesService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

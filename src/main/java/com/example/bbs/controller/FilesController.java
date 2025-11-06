package com.example.bbs.controller;

import com.example.bbs.entity.UploadedFiles;
import com.example.bbs.service.FilesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/files")
public class FilesController {

    private final FilesService filesService;

    public FilesController(FilesService filesService) {
        this.filesService = filesService;
    }

    // ✅ 파일 업로드
    @PostMapping
    public ResponseEntity<UploadedFiles> uploadFile(@PathVariable Long postId,
                                                    @RequestParam("file") MultipartFile file) throws IOException {
        UploadedFiles savedFile = filesService.saveFile(postId, file);
        return ResponseEntity.ok(savedFile);
    }

    // ✅ 게시글의 파일 목록
    @GetMapping
    public List<UploadedFiles> getFiles(@PathVariable Long postId) {
        return filesService.getFilesByPost(postId);
    }

    // ✅ 파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        filesService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

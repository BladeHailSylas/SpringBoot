package com.example.bbs.controller;

import com.example.bbs.dto.MediaRequest;
import com.example.bbs.dto.MediaResponse;
import com.example.bbs.entity.Media;
import com.example.bbs.service.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * 특정 게시글에 미디어 업로드
     * 실제 파일은 uploads/{postId}/ 에 저장되고,
     * DB에는 Media 엔티티로 저장됩니다.
     */
    @PostMapping("/{postId}/media")
    public ResponseEntity<MediaResponse> uploadMedia(
            @PathVariable Long postId,
            @RequestPart("file") MultipartFile file,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "thumbnail", required = false) Boolean thumbnail) throws IOException {

        // DTO 생성 후 Service로 전달
        MediaRequest request = new MediaRequest(file, description, thumbnail != null && thumbnail);
        Media saved = mediaService.saveFile(postId, request);
        return ResponseEntity.ok(new MediaResponse(saved));
    }

    @PostMapping("/media/temp")
    public ResponseEntity<MediaResponse> uploadTempMedia(@RequestParam("file") MultipartFile file) throws IOException {
        Media saved = mediaService.saveTempFile(file);
        return ResponseEntity.ok(new MediaResponse(saved));
    }

    @GetMapping("/{postId}/media")
    public ResponseEntity<List<MediaResponse>> getFiles(@PathVariable Long postId) {
        List<MediaResponse> mediaList = mediaService.getFilesByPost(postId)
                .stream()
                .map(MediaResponse::new)
                .toList();
        return ResponseEntity.ok(mediaList);
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/media/{fileId}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) throws IOException {
        mediaService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}

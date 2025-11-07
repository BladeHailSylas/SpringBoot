package com.example.bbs.dto;

import org.springframework.web.multipart.MultipartFile;

public class MediaRequest {

    private MultipartFile file;
    private String description;  // 이미지 설명 or 캡션
    private boolean thumbnail;   // 대표 이미지 여부

    public MediaRequest() {}

    public MediaRequest(MultipartFile file, String description, boolean thumbnail) {
        this.file = file;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isThumbnail() { return thumbnail; }
    public void setThumbnail(boolean thumbnail) { this.thumbnail = thumbnail; }
}

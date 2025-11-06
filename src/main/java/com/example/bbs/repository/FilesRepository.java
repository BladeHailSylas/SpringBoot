package com.example.bbs.repository;

import com.example.bbs.entity.UploadedFiles;
import com.example.bbs.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FilesRepository extends JpaRepository<UploadedFiles, Long> {
    List<UploadedFiles> findByPost(Post post);
}

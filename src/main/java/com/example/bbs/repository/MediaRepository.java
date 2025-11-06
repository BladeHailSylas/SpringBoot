package com.example.bbs.repository;

import com.example.bbs.entity.Media;
import com.example.bbs.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    List<Media> findByPost(Post post);
}

package com.example.bbs.repository;

import com.example.bbs.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByHiddenFalse();  // 숨기지 않은 게시글만 조회
}
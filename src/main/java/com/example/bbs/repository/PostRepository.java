package com.example.bbs.repository;

import com.example.bbs.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByHiddenFalseOrderByIdDesc(Pageable pageable);  // 숨기지 않은 게시글만 조회
}
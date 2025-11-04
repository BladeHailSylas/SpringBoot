package com.example.bbs.controller;

import com.example.bbs.entity.Post;
import com.example.bbs.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<Post> getAll() {
        return postService.getAllVisiblePosts();
    }

    @GetMapping("/{id}")
    public Optional<Post> getById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @PutMapping("/{id}")
    public Optional<Post> update(@PathVariable Long id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @PatchMapping("/{id}/hide")
    public String hide(@PathVariable Long id) {
        return postService.hidePost(id) ? "✅ 숨김 처리 완료" : "❌ 게시글을 찾을 수 없습니다.";
    }
}
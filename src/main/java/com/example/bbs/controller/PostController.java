package com.example.bbs.controller;

import com.example.bbs.dto.PostRequest;
import com.example.bbs.entity.Post;
import com.example.bbs.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 검증 실패 시
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        // 검증 통과 시
        Post post = new Post(request.getTitle(), request.getContent(), request.getAuthor());
        Post saved = postService.createPost(post);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @Valid @RequestBody PostRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        return postService.updatePost(id, new Post(request.getTitle(), request.getContent(), request.getAuthor()))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> hidePost(@PathVariable Long id) {
        boolean result = postService.hidePost(id);
        return result ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
    @PatchMapping("/{id}/hide")
    public String hide(@PathVariable Long id) {
        return postService.hidePost(id) ? "✅ 게시글이 삭제되었습니다." : "❌ 게시글을 찾을 수 없습니다.";
    }
}
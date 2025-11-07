package com.example.bbs.controller;

import com.example.bbs.dto.PostRequest;
import com.example.bbs.dto.PostResponse;
import com.example.bbs.entity.Post;
import com.example.bbs.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Page<PostResponse> getAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postService.getAllVisiblePosts(pageable);
        return posts.map(PostResponse::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(PostResponse::new)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostRequest request,
                                        BindingResult bindingResult,
                                        HttpServletRequest servletRequest) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(err ->
                    errors.put(err.getField(), err.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        // ✅ IP 추출
        String clientIp = servletRequest.getHeader("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = servletRequest.getRemoteAddr();
        }

        Post post = new Post(request.getTitle(), request.getContent(), request.getAuthor());
        post.setIpAddress(clientIp); // 새 필드에 저장

        Post saved = postService.createPost(post);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
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
package com.example.bbs.service;

import com.example.bbs.entity.Post;
import com.example.bbs.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> getAllVisiblePosts(Pageable pageable) {
        return postRepository.findByHiddenFalseOrderByIdDesc(pageable);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Optional<Post> updatePost(Long id, Post updated) {
        return postRepository.findById(id).map(post -> {
            post.setTitle(updated.getTitle());
            post.setContent(updated.getContent());
            post.setAuthor(updated.getAuthor());
            return postRepository.save(post);
        });
    }

    public boolean hidePost(Long id) {
        return postRepository.findById(id).map(post -> {
            post.setHidden(true);
            postRepository.save(post);
            return true;
        }).orElse(false);
    }
}
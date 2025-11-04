package com.example.bbs.service;

import com.example.bbs.entity.Comment;
import com.example.bbs.entity.Post;
import com.example.bbs.repository.CommentRepository;
import com.example.bbs.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return postRepository.findById(postId)
                .map(commentRepository::findByPostAndHiddenFalse)
                .orElse(List.of());
    }

    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow();
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public boolean hideComment(Long id) {
        return commentRepository.findById(id).map(c -> {
            c.setHidden(true);
            commentRepository.save(c);
            return true;
        }).orElse(false);
    }
}

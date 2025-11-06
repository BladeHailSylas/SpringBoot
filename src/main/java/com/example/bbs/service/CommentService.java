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

    // 루트 댓글 목록
    public List<Comment> getRootComments(Long postId) {
        return commentRepository.findByPostIdAndParentCommentIsNullOrderByCreatedAtAsc(postId);
    }

    // 특정 댓글의 대댓글 목록
    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findByParentCommentIdOrderByCreatedAtAsc(parentId);
    }

    // 댓글 추가 (일반)
    public Comment addComment(Long postId, Comment comment) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    // 대댓글 추가
    public Comment addReply(Long postId, Long parentId, Comment reply) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
        reply.setPost(post);
        reply.setParentComment(parent);
        return commentRepository.save(reply);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}

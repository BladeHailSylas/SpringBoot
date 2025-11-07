package com.example.bbs.service;

import com.example.bbs.entity.Comment;
import com.example.bbs.entity.Post;
import com.example.bbs.repository.CommentRepository;
import com.example.bbs.repository.PostRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public List<Comment> getRootComments(Long postId) {
        return commentRepository.findByPostIdAndParentCommentIsNullAndHiddenFalseOrderByCreatedAtAsc(postId);
    }

    // 특정 댓글의 대댓글 목록
    @Transactional
    public List<Comment> getReplies(Long parentId) {
        return commentRepository.findByParentCommentIdAndHiddenFalseOrderByCreatedAtAsc(parentId);
    }

    // 댓글 추가 (일반)
    @Transactional
    public Comment addComment(Long postId, String author, String content, String ipAddress) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setAuthor(author);
        comment.setContent(content);
        comment.setIpAddress(ipAddress);

        return commentRepository.save(comment);
    }

    // 대댓글 추가
    @Transactional
    public Comment addReply(Long postId, Long parentId, String author, String content, String ipAddress) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

        Comment reply = new Comment();
        reply.setPost(post);
        reply.setParentComment(parent);
        reply.setAuthor(author);
        reply.setContent(content);
        reply.setIpAddress(ipAddress);

        return commentRepository.save(reply);
    }
    @Transactional
    public void deleteComment(Long id) {
        commentRepository.findById(id).ifPresent(comment -> {
            comment.setHidden(true);
            commentRepository.save(comment);
        });
        System.out.println("Hidden updated in DB: " + commentRepository.findById(id).get().isHidden());
    }
}

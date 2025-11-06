package com.example.bbs.service;

import com.example.bbs.entity.Comments;
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
    public List<Comments> getRootComments(Long postId) {
        return commentRepository.findByPostIdAndParentCommentIsNullOrderByCreatedAtAsc(postId);
    }

    // 특정 댓글의 대댓글 목록
    public List<Comments> getReplies(Long parentId) {
        return commentRepository.findByParentCommentIdOrderByCreatedAtAsc(parentId);
    }

    // 댓글 추가 (일반)
    public Comments addComment(Long postId, String author, String content, String ipAddress) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comments comments = new Comments();
        comments.setPost(post);
        comments.setAuthor(author);
        comments.setContent(content);
        comments.setIpAddress(ipAddress);

        return commentRepository.save(comments);
    }

    // 대댓글 추가
    public Comments addReply(Long postId, Long parentId, String author, String content, String ipAddress) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        Comments parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));

        Comments reply = new Comments();
        reply.setPost(post);
        reply.setParentComment(parent);
        reply.setAuthor(author);
        reply.setContent(content);
        reply.setIpAddress(ipAddress);

        return commentRepository.save(reply);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

}

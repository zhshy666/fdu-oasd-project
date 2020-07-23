package com.oasd.backend.service;

import com.oasd.backend.domain.Comment;
import com.oasd.backend.repository.CommentFavorRepo;
import com.oasd.backend.repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private CommentRepo commentRepo;
    private CommentFavorRepo commentFavorRepo;

    @Autowired
    public CommentService(CommentRepo commentRepo, CommentFavorRepo commentFavorRepo) {
        this.commentRepo = commentRepo;
        this.commentFavorRepo = commentFavorRepo;
    }

    public List<Comment> getComments(int imageId) {
        return commentRepo.findCommentsByImageId(imageId);
    }

    public void postComment(String username, String content, int imageId) {
        Comment comment = new Comment();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        String time = formatter.format(date);
        comment.setTime(time);
        comment.setUsername(username);
        comment.setImageId(imageId);
        comment.setHeat(0);
        comment.setContent(content);
        commentRepo.insertComment(comment);
    }

    public boolean isFavor(int userId, int commentId) {
        return commentFavorRepo.findCommentFavor(userId, commentId);
    }

    public String removeFavor(int userId, int commentId) {
        return commentFavorRepo.deleteFavorByUserIdAndCommentId(userId, commentId);
    }

    public void modifyHeat(int commentId, int i) {
        commentRepo.updateHeatByCommentId(commentId, i);
    }

    public String addFavor(int userId, int commentId) {
        return commentFavorRepo.addFavorByUserIdAndCommentId(userId, commentId);
    }
}

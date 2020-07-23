package com.oasd.backend.controller;

import com.oasd.backend.controller.request.GetCommentsRequest;
import com.oasd.backend.controller.request.ImageDetailRequest;
import com.oasd.backend.controller.request.PostCommentRequest;
import com.oasd.backend.controller.request.UpdateCommentRequest;
import com.oasd.backend.domain.Comment;
import com.oasd.backend.domain.TravelUser;
import com.oasd.backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/postComment")
    public ResponseEntity<?> postComment(@RequestBody PostCommentRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        String content = request.getContent();
        int imageId = request.getImageId();
        commentService.postComment(username, content, imageId);
        return ResponseEntity.ok("");
    }

    @PostMapping("/getComments")
    public ResponseEntity<?> getComments(@RequestBody GetCommentsRequest request){
        int imageId = request.getImageId();
        List<Comment> comments = commentService.getComments(imageId);
        if(request.getLogin()) {
            TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // is favor or not
            for (Comment comment : comments) {
                comment.setFavor(commentService.isFavor(user.getId(), comment.getCommentId()));
            }
        }
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/cancelLike")
    public ResponseEntity<?> cancelLike(@RequestBody UpdateCommentRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = commentService.removeFavor(user.getId(), request.getCommentId());
        commentService.modifyHeat(request.getCommentId(), -1);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/addLike")
    public ResponseEntity<?> addLike(@RequestBody UpdateCommentRequest request){
        TravelUser user = (TravelUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String message = commentService.addFavor(user.getId(), request.getCommentId());
        commentService.modifyHeat(request.getCommentId(), 1);
        return ResponseEntity.ok(message);
    }
}

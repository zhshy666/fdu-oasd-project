package com.oasd.backend.controller.request;

public class UpdateCommentRequest {
    private int commentId;

    public UpdateCommentRequest() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
}

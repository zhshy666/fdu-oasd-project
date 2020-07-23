package com.oasd.backend.controller.request;

public class PostCommentRequest {
    private int imageId;
    private String content;

    public PostCommentRequest() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

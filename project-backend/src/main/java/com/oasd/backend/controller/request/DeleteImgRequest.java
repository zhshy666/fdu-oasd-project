package com.oasd.backend.controller.request;

public class DeleteImgRequest {
    private int imageId;
    private String url;

    public DeleteImgRequest() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

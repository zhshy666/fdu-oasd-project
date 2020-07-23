package com.oasd.backend.controller.request;

public class GetCommentsRequest {
    private int imageId;
    private boolean login;

    public GetCommentsRequest() {
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean getLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}

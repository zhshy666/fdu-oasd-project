package com.oasd.backend.controller.request;

public class ChangeVisibilityRequest {
    private String isPublic;

    public ChangeVisibilityRequest() {
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }
}

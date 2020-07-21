package com.oasd.backend.controller.request;

public class UpdateMessageRequest {
    private int messageId;

    public UpdateMessageRequest() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}

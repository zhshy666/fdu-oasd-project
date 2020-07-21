package com.oasd.backend.controller.request;

public class MarkAsReadRequest {
    private int messageId;

    public MarkAsReadRequest() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}

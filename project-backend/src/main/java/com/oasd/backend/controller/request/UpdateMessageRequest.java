package com.oasd.backend.controller.request;

public class UpdateMessageRequest {
    private int messageId;
    private int to;

    public UpdateMessageRequest() {
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }
}

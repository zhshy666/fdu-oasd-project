package com.oasd.backend.controller.request;

public class SendRequest {
    private int[] sendUsers;

    public SendRequest() {
    }

    public int[] getSendUsers() {
        return sendUsers;
    }

    public void setSendUsers(int[] sendUsers) {
        this.sendUsers = sendUsers;
    }
}

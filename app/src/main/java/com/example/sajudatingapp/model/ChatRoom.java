package com.example.sajudatingapp.model;

public class ChatRoom {
    private User partner;
    private String lastMessage;
    private boolean isPending; // For "acceptance required" state

    public ChatRoom(User partner, String lastMessage, boolean isPending) {
        this.partner = partner;
        this.lastMessage = lastMessage;
        this.isPending = isPending;
    }

    // Getters
    public User getPartner() {
        return partner;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public boolean isPending() {
        return isPending;
    }
}

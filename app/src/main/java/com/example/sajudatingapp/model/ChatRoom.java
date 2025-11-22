package com.example.sajudatingapp.model;

import java.util.Date;

public class ChatRoom {
    private Long id;
    private Long userId;
    private Long partnerId;
    private User partner;
    private String lastMessage;
    private Date lastMessageTime;
    private boolean isPending; // For "acceptance required" state
    private Date createdAt;

    public ChatRoom(User partner, String lastMessage, boolean isPending) {
        this.partner = partner;
        this.lastMessage = lastMessage;
        this.isPending = isPending;
        this.createdAt = new Date();
    }

    public ChatRoom(Long id, Long userId, Long partnerId, String lastMessage, Date lastMessageTime, boolean isPending, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.partnerId = partnerId;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.isPending = isPending;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public User getPartner() {
        return partner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(Date lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

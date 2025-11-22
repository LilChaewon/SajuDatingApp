package com.example.sajudatingapp.model;

import java.util.Date;

public class Message {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private boolean isSentByUser;
    private Date timestamp;
    private boolean isRead;

    public Message(String content, boolean isSentByUser) {
        this.content = content;
        this.isSentByUser = isSentByUser;
        this.timestamp = new Date();
        this.isRead = false;
    }

    public Message(Long chatRoomId, Long senderId, String content, boolean isSentByUser, Date timestamp, boolean isRead) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.isSentByUser = isSentByUser;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public Message(Long id, Long chatRoomId, Long senderId, String content, boolean isSentByUser, Date timestamp, boolean isRead) {
        this.id = id;
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.isSentByUser = isSentByUser;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }
}

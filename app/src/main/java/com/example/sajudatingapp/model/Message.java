package com.example.sajudatingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {
    @SerializedName("id")
    private int id;

    @SerializedName("chat_room_id")
    private int chatRoomId;

    @SerializedName("sender_id")
    private int senderId;

    @SerializedName("content")
    private String content;

    @SerializedName("sent_at")
    private Date sentAt;

    // Transient field for UI logic (not part of API response)
    private transient boolean isSentByUser;

    // Constructor for messages received from the server (Gson handles id, chatRoomId, senderId, sentAt)
    public Message(String content, int senderId, Date sentAt) {
        this.content = content;
        this.senderId = senderId;
        this.sentAt = sentAt;
    }

    // Constructor for local messages (sent by current user)
    public Message(int chatRoomId, int senderId, String content, Date sentAt, boolean isSentByUser) {
        this.chatRoomId = chatRoomId;
        this.senderId = senderId;
        this.content = content;
        this.sentAt = sentAt;
        this.isSentByUser = isSentByUser;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getChatRoomId() {
        return chatRoomId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public boolean isSentByUser() {
        return isSentByUser;
    }

    // Setter for isSentByUser (to be set after determining current user)
    public void setSentByUser(boolean sentByUser) {
        isSentByUser = sentByUser;
    }
}

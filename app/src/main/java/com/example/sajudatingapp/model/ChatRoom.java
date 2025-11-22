package com.example.sajudatingapp.model;

import com.google.gson.annotations.SerializedName;

public class ChatRoom {

    @SerializedName("id")
    private int id;

    @SerializedName("status")
    private String status;

    @SerializedName("other_user_id")
    private int otherUserId;

    @SerializedName("other_user_name")
    private String otherUserName;
    
    @SerializedName("other_user_profile_picture_url")
    private String otherUserProfilePictureUrl;

    // Getters
    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public int getOtherUserId() {
        return otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public String getOtherUserProfilePictureUrl() {
        return otherUserProfilePictureUrl;
    }
}

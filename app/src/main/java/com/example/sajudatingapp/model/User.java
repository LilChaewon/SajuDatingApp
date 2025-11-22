package com.example.sajudatingapp.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("age")
    private int age;

    @SerializedName("location")
    private String location;

    @SerializedName("job")
    private String job;

    @SerializedName("bio")
    private String bio;

    @SerializedName("profile_picture_url")
    private String profilePictureUrl;

    // A compatibility score field that can be set locally after a separate API call
    private transient int compatibilityScore;

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public String getJob() {
        return job;
    }

    public String getBio() {
        return bio;
    }
    
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public int getCompatibilityScore() {
        return compatibilityScore;
    }

    public void setCompatibilityScore(int compatibilityScore) {
        this.compatibilityScore = compatibilityScore;
    }
}

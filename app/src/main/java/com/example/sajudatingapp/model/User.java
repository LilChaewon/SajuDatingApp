package com.example.sajudatingapp.model;

public class User {
    private String name;
    private int age;
    private String location;
    private String job;
    private String bio;
    private int compatibilityScore;

    public User(String name, int age, String location, String job, String bio, int compatibilityScore) {
        this.name = name;
        this.age = age;
        this.location = location;
        this.job = job;
        this.bio = bio;
        this.compatibilityScore = compatibilityScore;
    }

    // Getters
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

    public int getCompatibilityScore() {
        return compatibilityScore;
    }
}

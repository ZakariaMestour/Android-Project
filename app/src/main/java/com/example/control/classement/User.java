package com.example.control.classement;

public class User {
    private String userId;
    private String username;
    private int score;

    // Constructeur vide nécessaire pour Firebase
    public User() {
    }

    public User(String userId, String username, int score) {
        this.userId = userId;
        this.username = username;
        this.score = score;
    }

    // Getters et setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

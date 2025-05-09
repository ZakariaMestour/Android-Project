package com.example.control.classement;

public class User {
    private String userId;
    private String username;
    private String email;

    private int score;

    // Constructeur vide nécessaire pour Firebase
    public User() {
    }

    public User(String userId, String username,String email, int score) {
        this.userId = userId;
        this.username = username;
        this.score = score;
        this.email=email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

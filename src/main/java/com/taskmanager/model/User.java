package com.taskmanager.model;

/**
 * User entity class representing a user in the system
 */
public class User {
    // Instance variables
    private int userId;         // Unique identifier for the user
    private String username;    // User's username
    private String password;    // User's password
    private String email;       // User's email address


    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
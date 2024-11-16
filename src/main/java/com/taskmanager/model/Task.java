package com.taskmanager.model;

public class Task {
    private int taskId;
    private String title;
    private String description;
    private String status;  // "Pending", "In Progress", "Completed"
    private int assignedTo; // user_id of assigned user
    private String createdDate;

    public Task(String title, String description, String status, int assignedTo) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignedTo = assignedTo;
    }

    // Getters and Setters
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getAssignedTo() { return assignedTo; }
    public void setAssignedTo(int assignedTo) { this.assignedTo = assignedTo; }

    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}
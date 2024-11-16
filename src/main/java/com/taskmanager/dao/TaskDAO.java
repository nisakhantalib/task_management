package com.taskmanager.dao;

import com.taskmanager.model.Task;
import com.taskmanager.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public void createTask(Task task) throws SQLException {
        // SQL query to insert a new task with required fields
        String sql = "INSERT INTO Tasks (title, description, status, assigned_to) VALUES (?, ?, ?, ?)";

        // Use try-with-resources to automatically close database resource
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Set parameters for the prepared statement using task object properties
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setInt(4, task.getAssignedTo());
            pstmt.executeUpdate();
        }
    }

    public List<Task> getAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM Tasks";

        try (Connection conn = DatabaseConnection.getConnection();
             //it's used for static SQL that doesn't need parameters (As opposed to pstmt)
             Statement stmt = conn.createStatement();
             // execute query and store the result in RS, which is a curesor that can be iterated
             ResultSet rs = stmt.executeQuery(sql)) {

            // rs.next() moves cursor to next row and returns false when no more rows
            while (rs.next()) {
                // Create new Task object from current row's data
                Task task = new Task(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getInt("assigned_to")
                );
                // Set additional fields that aren't part of the constructor
                task.setTaskId(rs.getInt("task_id"));
                task.setCreatedDate(rs.getString("created_date"));
                // Add task object to list
                tasks.add(task);
            }
        }
        return tasks;
    }

    public List<Task> getTasksByStatus(String status) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        // SQL query with WHERE clause to filter by status
        String sql = "SELECT * FROM Tasks WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Task task = new Task(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("status"),
                        rs.getInt("assigned_to")
                );
                task.setTaskId(rs.getInt("task_id"));
                task.setCreatedDate(rs.getString("created_date"));
                tasks.add(task);
            }
        }
        return tasks;
    }

    public void updateTask(Task task) throws SQLException {
        String sql = "UPDATE Tasks SET title = ?, description = ?, status = ?, assigned_to = ? WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setInt(4, task.getAssignedTo());
            pstmt.setInt(5, task.getTaskId());
            pstmt.executeUpdate();
        }
    }

    public void deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM Tasks WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        }
    }
}
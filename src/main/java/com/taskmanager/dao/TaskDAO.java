package com.taskmanager.dao;

import com.taskmanager.model.Task;
import com.taskmanager.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public void createTask(Task task) throws SQLException {
        String sql = "INSERT INTO Tasks (title, description, status, assigned_to) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

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

    public List<Task> getTasksByStatus(String status) throws SQLException {
        List<Task> tasks = new ArrayList<>();
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
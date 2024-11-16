package com.taskmanager.dao;

import com.taskmanager.model.User;
import com.taskmanager.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void createUser(User user) throws SQLException {
        // SQL query with parameterized values for security against SQL injection
        String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";

        // Use try-with-resources to automatically close database resources
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
            // Index starts at 1, matches the order in SQL query
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());

            // Execute the insert
            pstmt.executeUpdate();
        }
    }


    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Create new User object from current row's data
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                // Set the database-generated user ID
                user.setUserId(rs.getInt("user_id"));
                // Add the populated user object to list
                users.add(user);
            }
        }
        return users;
    }

//    public void deleteUser(int userId) throws SQLException{
//
//        String sql="DELETE FROM Users WHERE userId=?";
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, userId);
//            // Execute the delete
//            pstmt.executeUpdate();
//
//        }
//
//
//
//    }
}
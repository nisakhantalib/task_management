package com.taskmanager.dao;

import com.taskmanager.model.User;
import com.taskmanager.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void createUser(User user) throws SQLException {
        String sql = "INSERT INTO Users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the prepared statement
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
                User user = new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email")
                );
                user.setUserId(rs.getInt("user_id"));
                users.add(user);
            }
        }
        return users;
    }
}
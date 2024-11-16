package com.taskmanager.test;

import java.sql.Connection;
import java.sql.SQLException;
import com.taskmanager.util.DatabaseConnection;
import com.taskmanager.model.User;
import com.taskmanager.dao.UserDAO;

/**
 * Test class to verify database setup and basic operations
 */
public class TestSQLite {
    public static void main(String[] args) {
        try {
            // Test database connection
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("SQLite connection successful!");

            // Initialize database tables
            DatabaseConnection.initializeDatabase();
            System.out.println("Database tables created successfully!");

            // Test user creation
            UserDAO userDAO = new UserDAO();
            User testUser = new User("testuser", "password123", "test@email.com");
            userDAO.createUser(testUser);
            System.out.println("Test user created successfully!");

            // Test user retrieval
            System.out.println("\nAll users in database:");
            for (User user : userDAO.getAllUsers()) {
                System.out.println("Username: " + user.getUsername() + ", Email: " + user.getEmail());
            }

            conn.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
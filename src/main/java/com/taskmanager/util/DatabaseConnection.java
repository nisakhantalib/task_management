package com.taskmanager.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:taskmanager.db";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() throws SQLException {
        String createUsersTable = "CREATE TABLE IF NOT EXISTS Users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "email TEXT UNIQUE NOT NULL," +
                "created_date TEXT DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String createTasksTable = "CREATE TABLE IF NOT EXISTS Tasks (" +
                "task_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "status TEXT CHECK(status IN ('Pending', 'In Progress', 'Completed')) DEFAULT 'Pending'," +
                "assigned_to INTEGER," +
                "created_date TEXT DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (assigned_to) REFERENCES Users(user_id)" +
                ")";

        try (Connection conn = getConnection()) {
            conn.createStatement().execute(createUsersTable);
            conn.createStatement().execute(createTasksTable);
        }
    }
}
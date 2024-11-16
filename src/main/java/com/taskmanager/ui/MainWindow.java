package com.taskmanager.ui;

import javax.swing.*;
import com.taskmanager.util.DatabaseConnection;

public class MainWindow extends JFrame {
    private UserManagementPanel userPanel;
    private TaskPanel taskPanel;

    public MainWindow() {
        setTitle("Task Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize database
        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Database initialization failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Create tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Create panels in correct order
        taskPanel = new TaskPanel();
        userPanel = new UserManagementPanel(() -> taskPanel.refreshUserComboBox());

        // Add tabs
        tabbedPane.addTab("Users", userPanel);
        tabbedPane.addTab("Tasks", taskPanel);

        add(tabbedPane);

        // Center on screen
        setLocationRelativeTo(null);
    }




    public static void main(String[] args) {
        // Run UI in Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new MainWindow().setVisible(true);
        });
    }
}
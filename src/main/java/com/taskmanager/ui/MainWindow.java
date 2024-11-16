package com.taskmanager.ui;

import javax.swing.*;
import com.taskmanager.util.DatabaseConnection;


/**
 * MainWindow is the primary container for the Task Management System UI.

 * Component Hierarchy:
 * MainWindow (JFrame)
 *   -> JTabbedPane
 *        ->UserManagementPanel (Tab 1)
 *        -> TaskPanel (Tab 2)
 */


public class MainWindow extends JFrame {
    private UserManagementPanel userPanel;
    private TaskPanel taskPanel;

    public MainWindow() {
        setTitle("Task Management System");
        setSize(800, 600);
        // EXIT_ON_CLOSE means the application will terminate when window is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // This ensures data access is available when UI components need it
        try {
            DatabaseConnection.initializeDatabase();
        } catch (Exception e) {
            // If database initialization fails, show error dialog
            // Parameters:
            // 1. this - centers dialog over main window
            // 2. error message
            // 3. dialog title
            // 4. type of message (affects icon shown)
            JOptionPane.showMessageDialog(this,
                    "Database initialization failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // JTabbedPane allows switching between different panels using tabs
        JTabbedPane tabbedPane = new JTabbedPane();

        // Initialize panels in specific order
        // TaskPanel must exist before UserManagementPanel because of the refresh callback
        taskPanel = new TaskPanel();

        // Create UserManagementPanel with lambda callback
        // This callback will be executed whenever users are modified
        //the callback function calls refreshUserComboBox() on taskPanel
        // This updates the list of users shown in the task panel
        userPanel = new UserManagementPanel(taskPanel);



        // Add tabs
        tabbedPane.addTab("Users", userPanel);
        tabbedPane.addTab("Tasks", taskPanel);

        add(tabbedPane);

        // Center on screen
        setLocationRelativeTo(null);
    }



    //Application entry point
    public static void main(String[] args) {
        // Run UI in Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // This makes the application look native to the platform it's running on
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            new MainWindow().setVisible(true);
        });
    }
}
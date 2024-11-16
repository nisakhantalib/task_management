// UserManagementPanel.java
package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.User;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

/**
 * Panel for managing users in the Task Management System.
 * Provides functionality to add new users and display existing users in a table.
 */
public class UserManagementPanel extends JPanel {
    // UI Components
    private JTextField usernameField;      // Field for entering username
    private JPasswordField passwordField;   // Secure field for entering password
    private JTextField emailField;          // Field for entering email
    private JTable userTable;              // Table to display users
    private UserDAO userDAO;               // Data Access Object for user operations
    private Runnable onUserUpdated;

    public UserManagementPanel(Runnable onUserUpdated) {
        this.onUserUpdated = onUserUpdated;
        userDAO = new UserDAO();
        // Set layout with 10-pixel spacing
        setLayout(new BorderLayout(10, 10));

        // Create and add the form panel to the top
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.NORTH);

        // Initialize and setup the user table
        createUserTable();
        refreshUserTable();
    }

    /**
     * Creates the form panel for adding new users
     * @return JPanel containing the input form
     */
    private JPanel createFormPanel() {
        // Create panel with 4 rows, 2 columns, and 5-pixel spacing
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New User"));

        // Initialize input fields
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);

        // Add labels and fields to the panel
        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        // Create and configure Add button
        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser()); // Lambda for click event

        // Add empty panel for spacing and the button
        formPanel.add(new JPanel());
        formPanel.add(addButton);

        return formPanel;
    }

    /**
     * Creates and initializes the user table
     */
    private void createUserTable() {
        // Define table columns
        String[] columns = {"ID", "Username", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        userTable = new JTable(model);
        // Add table to scroll pane and add to panel
        add(new JScrollPane(userTable), BorderLayout.CENTER);
    }

    /**
     * Handles the addition of a new user
     * Validates input and shows appropriate messages
     */
    private void addUser() {
        // Get input values
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        // Validate input fields
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Create and save new user
            User user = new User(username, password, email);
            userDAO.createUser(user);

            // Clear form and refresh table on success
            clearForm();
            refreshUserTable();

            if (onUserUpdated != null) {
                onUserUpdated.run();  // Refresh task panel's user dropdown
            }
            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding user: " + e.getMessage());
        }
    }

    /**
     * Clears all input fields in the form
     */
    private void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        emailField.setText("");
    }

    /**
     * Refreshes the user table with current data from database
     */
    private void refreshUserTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            model.setRowCount(0); // Clear existing rows

            // Add all users from database to table
            for (User user : userDAO.getAllUsers()) {
                model.addRow(new Object[]{
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail()
                });
            }
        } catch (SQLException e) {
            // Show error message if refresh fails
            JOptionPane.showMessageDialog(this,
                    "Error refreshing table: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
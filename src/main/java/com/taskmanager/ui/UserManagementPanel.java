package com.taskmanager.ui;

import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.User;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * Panel for managing users in the Task Management System.
 * Provides functionality to add new users and display existing users in a table.
 */
public class UserManagementPanel extends JPanel {
    // UI Components
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private JTable userTable;
    private UserDAO userDAO;
    private TaskPanel taskPanel;  // Direct reference to TaskPanel

    // Regex pattern for validating email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // Regex pattern for validating password
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!~*()?]).{4,20}$";

    private static final String USERNAME_REGEX="^[a-zA-Z0-9_]{3,}$";

    public UserManagementPanel(TaskPanel taskPanel) {
        this.taskPanel = taskPanel;
        userDAO = new UserDAO();
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
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New User"));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        emailField = new JTextField(20);

        // Tooltips for input fields
        usernameField.setToolTipText("Enter a unique username");
        passwordField.setToolTipText("Enter a strong password");
        emailField.setToolTipText("Enter a valid email address");

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser());

        formPanel.add(new JPanel());
        formPanel.add(addButton);

        return formPanel;
    }

    /**
     * Creates and initializes the user table
     */
    private void createUserTable() {
        String[] columns = {"ID", "Username", "Email"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        userTable = new JTable(model);
        add(new JScrollPane(userTable), BorderLayout.CENTER);
    }

    /**
     * Handles the addition of a new user
     * Validates input and shows appropriate messages
     */
    private void addUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        // Validate username, password, and email
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "All fields are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!username.matches(USERNAME_REGEX)) {
            JOptionPane.showMessageDialog(this, "Username must be atleast 3 chars and can only contain letters, numbers, and underscores!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidPassword(password)) {
            JOptionPane.showMessageDialog(this,
                    "Password must be at least 4 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this,
                    "Invalid email format!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }


        try {
            User user = new User(username, password, email);
            userDAO.createUser(user);

            clearForm();
            refreshUserTable();

            // Directly call TaskPanel's method to refresh the user combo box
            if (taskPanel != null) {
                taskPanel.refreshUserComboBox();
            }

            JOptionPane.showMessageDialog(this, "User added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding user: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }
    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
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
     * Refreshes the user table with current data from the database
     */
    private void refreshUserTable() {
        try {
            DefaultTableModel model = (DefaultTableModel) userTable.getModel();
            model.setRowCount(0); // Clear existing rows

            for (User user : userDAO.getAllUsers()) {
                model.addRow(new Object[]{
                        user.getUserId(),
                        user.getUsername(),
                        user.getEmail()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error refreshing table: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

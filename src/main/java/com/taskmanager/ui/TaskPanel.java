package com.taskmanager.ui;

import com.taskmanager.dao.TaskDAO;
import com.taskmanager.dao.UserDAO;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TaskPanel extends JPanel {
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> statusCombo;
    private JComboBox<UserComboItem> assigneeCombo;
    private JTable taskTable;
    private TaskDAO taskDAO;
    private UserDAO userDAO;

    public TaskPanel() {
        // Instantiate DAO objects to interact with the database
        taskDAO = new TaskDAO();
        userDAO = new UserDAO();

        // Use BoxLayout for vertical arrangement
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Add New Task Form
        JPanel formPanel = createFormPanel();
        add(formPanel);

        // Add some vertical spacing
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Filter Panel
        JPanel filterPanel = createFilterPanel();
        add(filterPanel);

        // Add some vertical spacing
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Task Table
        createTaskTable();
        JScrollPane scrollPane = new JScrollPane(taskTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        add(scrollPane);

        // Initial data load
        refreshTaskTable(null);
    }

    private JPanel createFormPanel() {
        // Use GridBagLayout for flexible layout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Task"));
        GridBagConstraints gbc = new GridBagConstraints();
        // Padding between components
        gbc.insets = new Insets(5, 5, 5, 5);
        // Make components fill horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; //Col=0, Row=0
        formPanel.add(new JLabel("Title:"), gbc);

        titleField = new JTextField(30);
        gbc.gridx = 1; // Col=1
        gbc.weightx = 1.0; // Allow horizontal expansion
        formPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Description:"), gbc);

        // Create a text area with 3 rows and 30 columns
        descriptionArea = new JTextArea(3, 30);
        descriptionArea.setLineWrap(true);
        // Add a scroll pane for the text area
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(scrollPane, gbc);

        // Status
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Status:"), gbc);

        statusCombo = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed"});
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);

        // Assignee
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Assign To:"), gbc);

        // Dropdown for users
        assigneeCombo = new JComboBox<>();
        // Load users into the dropdown
        loadUsers();
        gbc.gridx = 1;
        formPanel.add(assigneeCombo, gbc);

        // Add button to submit the form and add a task
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(e -> addTask());
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(addButton, gbc);

        return formPanel;
    }

    private void createTaskTable() {
        String[] columns = {"ID", "Title", "Description", "Status", "Assigned To"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        taskTable = new JTable(model);

        // Set column widths
        taskTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        taskTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        taskTable.getColumnModel().getColumn(2).setPreferredWidth(300);
        taskTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        taskTable.getColumnModel().getColumn(4).setPreferredWidth(150);

        taskTable.setRowHeight(25);
        taskTable.getTableHeader().setReorderingAllowed(false);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Tasks"));

        filterPanel.add(new JLabel("Status: "));
        JComboBox<String> filterCombo = new JComboBox<>(
                new String[]{"All", "Pending", "In Progress", "Completed"});
        filterCombo.addActionListener(e -> {
            String status = (String)filterCombo.getSelectedItem();

            // Check if the selected status is "All".
            // If so, pass `null` to the `refreshTaskTable` method to load all tasks.
            // Otherwise, pass the selected status to filter tasks based on that status.
            refreshTaskTable("All".equals(status) ? null : status);
        });
        filterPanel.add(filterCombo);

        return filterPanel;
    }

    private void loadUsers() {
        try {
            assigneeCombo.removeAllItems();
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                assigneeCombo.addItem(new UserComboItem(user.getUserId(), user.getUsername()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading users: " + e.getMessage());
        }
    }

    private void addTask() {
        try {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String status = (String)statusCombo.getSelectedItem();
            UserComboItem selectedUser = (UserComboItem)assigneeCombo.getSelectedItem();

            if (title.isEmpty() || description.isEmpty() || selectedUser == null) {
                JOptionPane.showMessageDialog(this,
                        "All fields are required!");
                return;
            }

            Task task = new Task(title, description, status, selectedUser.getId());
            taskDAO.createTask(task);

            clearForm();
            refreshTaskTable(null);
            JOptionPane.showMessageDialog(this,
                    "Task added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error adding task: " + e.getMessage());
        }
    }

    private void refreshTaskTable(String status) {
        try {
            List<Task> tasks;
            if (status == null) {
                tasks = taskDAO.getAllTasks();
            } else {
                tasks = taskDAO.getTasksByStatus(status);
            }

            DefaultTableModel model = (DefaultTableModel)taskTable.getModel();
            model.setRowCount(0);

            for (Task task : tasks) {
                model.addRow(new Object[]{
                        task.getTaskId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        getUsernameById(task.getAssignedTo())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error refreshing tasks: " + e.getMessage());
        }
    }

    private String getUsernameById(int userId) {
        try {
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                if (user.getUserId() == userId) {
                    return user.getUsername();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private void clearForm() {
        titleField.setText("");
        descriptionArea.setText("");
        statusCombo.setSelectedIndex(0);
        if (assigneeCombo.getItemCount() > 0) {
            assigneeCombo.setSelectedIndex(0);
        }
    }

    private static class UserComboItem {
        private final int id;
        private final String name;

        public UserComboItem(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() { return id; }

        @Override
        public String toString() { return name; }
    }

    public void refreshUserComboBox() {
        try {
            assigneeCombo.removeAllItems();
            List<User> users = userDAO.getAllUsers();
            for (User user : users) {
                assigneeCombo.addItem(new UserComboItem(user.getUserId(), user.getUsername()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error refreshing users: " + e.getMessage());
        }
    }

}
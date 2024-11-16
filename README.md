```markdown
# Task Management System

A Java desktop application for managing tasks and users using SQLite database.

## Features

- User Management
  - Create and view users
  - User details include username, password, and email
- Task Management
  - Create and assign tasks to users
  - Set task status (Pending, In Progress, Completed)
  - Filter tasks by status

## Prerequisites

- Java 11 or higher
- Maven
- SQLite

## Installation & Setup

1. Clone the repository
```bash
git clone https://github.com/nisakhantalib/task_management.git
cd task-management-system
```

2. Build the project
```bash
mvn clean package
```

3. Run the application
```bash
java -jar target/task-management-system-1.0.jar
```

## Usage Guide

### Managing Users
1. Navigate to "Users" tab
2. Fill in user details (username, password, email)
3. Click "Add User"
4. View all users in the table below

### Managing Tasks
1. Navigate to "Tasks" tab
2. Fill in task details:
    - Title
    - Description
    - Status (Pending, In Progress, Completed)
    - Assign to user from dropdown
3. Click "Add Task"
4. Use status filter to view specific tasks

## Database Structure

### Users Table
- user_id (Primary Key)
- username
- password
- email
- created_date

### Tasks Table
- task_id (Primary Key)
- title
- description
- status
- assigned_to (Foreign Key to Users)
- created_date

## Implementation Details

- Built with Java Swing for desktop interface
- SQLite database for data persistence
- CRUD operations for both users and tasks
- Error handling for database operations
- Input validation for all forms

## Limitations

- Basic user interface
- No authentication system
- No data encryption
- Single-user application
- No concurrent access handling

## Future Improvements

- Add user authentication
- Implement password encryption
- Add task due dates
- Add task priority levels
- Improve UI/UX design
- Add data export functionality

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


-- Create Users table if it doesn't exist
CREATE TABLE IF NOT EXISTS Users (
    -- Primary key with autoincrement
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    -- Username must be unique and not null
    username TEXT UNIQUE NOT NULL,
    -- Password stored as text (should be hashed in production)
    password TEXT NOT NULL,
    -- Email must be unique and not null
    email TEXT UNIQUE NOT NULL,
    -- Automatically set to current timestamp
    created_date TEXT DEFAULT CURRENT_TIMESTAMP
);

-- Create Tasks table if it doesn't exist
CREATE TABLE IF NOT EXISTS Tasks (
    -- Primary key with autoincrement
    task_id INTEGER PRIMARY KEY AUTOINCREMENT,
    -- Task title cannot be null
    title TEXT NOT NULL,
    -- Description can be null
    description TEXT,
    -- Status with check constraint for valid values
    status TEXT CHECK(status IN ('Pending', 'In Progress', 'Completed')) DEFAULT 'Pending',
    -- Foreign key referencing Users table
    assigned_to INTEGER,
    -- Automatically set to current timestamp
    created_date TEXT DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assigned_to) REFERENCES Users(user_id)
);
# Student Registration System

## Introduction

This project is a student registration system that supports CRUD operations (Create, Read, Update, Delete) for managing student data in a MySQL database. It is built using Java, JDBC, and HikariCP and features an immutable implementation for the Student class.

## Project Structure

```
src/
│── org.example/
│   ├── StudentInterface.java
│   ├── StudentFactory.java
│   ├── util/
│   │   ├── DatabaseConfig.java
│   │   ├── StudentReader.java
│   ├── model/
│   │   ├── Student.java
│   │   ├── StudentDao.java
│   ├── pages/
│   │   ├── EditWindow 
│   │   ├── SearchWindow
│
└── README.md
```

## Features

✅ Immutable Student class: All fields are final, and values are set via Reflection.  
✅ Factory Pattern for creating Student instances.  
✅ Database connection management using HikariCP for better performance.  
✅ Automatic studentId generation based on year of entry + sequential number (e.g., 202511213001).  
✅ CRUD operations for managing students.  
✅ Reading student data from the database with StudentReader.

## Prerequisites

1. Install MySQL

2. Create the required database and table

```sql
CREATE DATABASE university;
USE university;

CREATE TABLE students (
    id BIGINT PRIMARY KEY,
    student_id VARCHAR(20) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth VARCHAR(10) NOT NULL,
    national_code VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(15) NOT NULL,
    address TEXT,
    major VARCHAR(50) NOT NULL,
    entry_date DATETIME NOT NULL
);
```

## Usage

### 1. Configure the Database (DatabaseConfig.java)

Before running the project, set up database connection details in DatabaseConfig.java:

```java
package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/university";
    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

🔹 Replace your_username and your_password with your actual database credentials.

### 2. Run the Project

#### Saving a Student to the Database

To add a new student, use StudentDao:

```java
StudentDao studentDao = new StudentDao();
StudentInterface student = StudentFactory.createStudent(null,null,"Ali", "Ahmadi", 
        "1382/06/26", "1234567890", "ali@example.com", 
        "09121234567", "Tehran, Iran", "Software Engineering", null);

studentDao.saveStudent(student);
```

Or Use Swing to use Project

` just run main.java to see :)`

## Author

✍ Reza

🔹 A developer passionate about computer system optimization.

## License

🚀 This project is released for Fun :)
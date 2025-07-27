#  Java Expense Tracker (Console-Based)

This is a **simple Java console application** that allows users to manage their personal expenses and allows an admin to manage users and their expense data.

## Features

### User Functionality
- Register a new account
- Login securely
- Add new expenses
- View all your expenses
- Export your expense data to a CSV file

###  Admin Functionality
- Login with credentials from the database
- View all registered users
- Change any user's password
- Export any user's expense data to CSV

## Technologies Used

- **Java (JDK 8 or above)**
- **MySQL (via XAMPP or standalone server)**
- **JDBC** (`mysql-connector-j-9.4.0.jar`)
- **Console-based UI**
- **CSV Export (via `FileWriter`)**

## 🗄️ Database Structure

### Database: `expense_tracker`

#### Table: `users`
| Column    | Type         |
|-----------|--------------|
| id        | INT (PK, AI) |
| username  | VARCHAR(50)  |
| password  | VARCHAR(100) |

#### Table: `expenses`
| Column     | Type          |
|------------|---------------|
| id         | INT (PK, AI)  |
| user_id    | INT (FK)      |
| amount     | DOUBLE        |
| category   | VARCHAR(50)   |
| date_spent | DATE          |
| description| TEXT          |

#### Table: `admin`
| Column   | Type         |
|----------|--------------|
| name     | VARCHAR(50)  |
| password | VARCHAR(100) |

> Make sure this table has at least one default admin row (e.g., name: `admin`, password: `admin123`)

##  Setup Instructions

1. Clone or download this repository.

2. Import the SQL schema in your MySQL using XAMPP or any other tool.

3. Update your `DBConnection.java` file with your correct database credentials:
```java
String url = "jdbc:mysql://localhost:3306/expense_tracker";
String user = "root";
String password = ""; // default for XAMPP

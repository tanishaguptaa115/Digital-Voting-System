# Java GUI-Based Voting System

This is a **Java-based Voting System** application with a graphical user interface (GUI) built using **Swing**. It connects to a **MySQL** database to manage voters, candidates, and vote records.

## 📌 Features

- **Voter Registration** (18+ age restriction)
- **Secure Voter Login**
- **Voting Panel** (vote only once)
- **Admin Login**
  - View registered voters
  - View election results
  - Reset all votes
- Clean and simple **GUI with custom background and icons**

## 🛠️ Technologies Used

- Java (JDK 8 or above)
- Swing for GUI
- MySQL for backend database
- JDBC for database connection

## 🔧 Prerequisites

- Java Development Kit (JDK)
- MySQL Server
- MySQL JDBC Driver (e.g., `mysql-connector-java-8.0.xx.jar`)

## ⚙️ Database Setup

1. Create a database named `votingsystem`.
2. Execute the following SQL to set up the required tables:

```sql
CREATE TABLE voters (
    id VARCHAR(255) PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    password VARCHAR(100),
    has_voted BOOLEAN DEFAULT 0
);

CREATE TABLE candidates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    symbol VARCHAR(50),
    vote_count INT DEFAULT 0
);

CREATE TABLE admins (
    email VARCHAR(100) PRIMARY KEY,
    password VARCHAR(100)
);

-- Insert default admin
INSERT INTO admins (email, password) VALUES ('admin@example.com', 'admin123');

You can also populate the candidates table manually.

🚀 How to Run
Clone this repository.

Ensure the JDBC driver .jar is in your classpath.

Update the image paths and database credentials in the v2.java file:

String url = "jdbc:mysql://localhost:3306/votingsystem";
String user = "root";
String password = "your_password_here";

Compile and run the program:

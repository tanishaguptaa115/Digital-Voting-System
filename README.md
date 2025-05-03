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
```

> You can also populate the `candidates` table manually.

## 🚀 How to Run

1. Clone this repository.
2. Ensure the JDBC driver `.jar` is in your classpath.
3. Update the **image paths** and **database credentials** in the `v2.java` file:
   ```java
   String url = "jdbc:mysql://localhost:3306/votingsystem";
   String user = "root";
   String password = "your_password_here";
   ```
4. Compile and run the program:

```bash
javac v2.java
java v2
```

## 🖼️ Images and Icons

Ensure the following image files are available and their paths are correct:

- `bg.jpg` (Main menu background)
- `voter.jpg` (Voter login icon)
- `admin.jpg` (Admin login icon)
- `reg.jpg` (Registration icon)

You can place them in a known location or embed them as resources.

## ✅ Functionality Flow

1. **Main Menu** → Choose to Vote, Register, Admin Login, or Exit.
2. **Register** → Inputs Name, Age, Password → Generates unique Voter ID.
3. **Voter Login** → Verifies credentials and voting eligibility.
4. **Vote** → Displays list of candidates → One vote allowed.
5. **Admin Login** → Admin can view voters, results, or reset the system.

## 🧠 Future Improvements

- Password encryption (currently plain text).
- Input validation enhancements.
- Candidate management via admin panel.
- Export results to CSV/PDF.

## 📄 License

This project is licensed under the MIT License.

---

> **Note:** Always use secure practices when handling passwords and sensitive data in production environments.

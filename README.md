# 📚 Library Management System (Java + MySQL)

This is a **desktop-based Library Management System (LMS)** application built using **Java Swing** for the graphical user interface **(GUI)** and **MySQL** as the backend database.  
It provides efficient management of library resources, including **books and user accounts**, with robust admin and user functionality.

---

## 🌟 Overview

The LMS is designed to simplify the daily operations of any library:
- **Admins** can add, update, delete, and view books and users, issue and return books, and reset the database.
- **Users** can log in to view available books and track their current borrowings and due dates.

The system is intuitive, user-friendly, and scalable — suitable for college, school, or home libraries.

---

## 🚀 Features

### 👨‍💼 Admin Functionality
- **View Books, Users, and Issued Books**  
  Easily monitor all resources and user activity.
- **Add / Update / Delete Books**  
  Manage the library’s collection with options to modify book details or remove/add books.
- **Add / Update / Delete Users**  
  Manage library members and staff, change credentials, or update roles.
- **Issue Books to Users**  
  Record each transaction with period, issue date, and due date.
- **Return Books**  
  Calculate fines automatically based on late returns (`₹10/day late fee`).
- **Reset Database**  
  Quickly reinitialize the database to a clean state with sample data.

### 👤 User Functionality
- **Login**  
  Secure access to user profile.
- **View Available Books**  
  Browse the catalog.
- **View My Books**  
  Track all books currently issued to them with due dates and fines.

---

## 🏗️ Architecture & Technologies Used

| Technology      | Purpose                           |
|-----------------|-----------------------------------|
| Java            | Core application programming      |
| Java Swing      | GUI (graphical interface)         |
| JDBC            | MySQL database connectivity       |
| MySQL           | Data storage and management       |
| SQL             | Table creation, CRUD operations   |
| DbUtils (rs2xml.jar) | Converts SQL results to JTable |
| Exception Handling | Stability and error management |
| Event Handling  | GUI button/action triggers        |
| Date/Time APIs  | Handling book issue/return dates  |
| Prepared Statements | Secure, injection-safe queries |
| MVC Pattern (implicit) | Separation of data and UI  |

---

## 🛒 Database Schema

The **library** database is automatically created/reset by the app. Tables include:

**users**  
| UID | Username | Password | Admin (boolean) |

**books**  
| BID | BName | Genre | Price (integer) |

**issue**  
| IID | UID | BID | IssueDate (dd-MM-yyyy) | Period (days) | ReturnDate (dd-MM-yyyy) | Fine (integer) |

---
 
## 🔐 Default Login Credentials
Admins and users can use the following credentials for initial access:

| Role   | Username | Password |
|--------|----------|----------|
| Admin  | admin    | admin    |
| User   | john     | john123  |
| User   | sarah    | sarah123 |

---

## 🖥️ How to Use

1. **Login** as an Admin or a User.
2. **Admins** can:
   - View all data (books, users, issues)
   - Add, edit, or delete books/users
   - Issue and return books
   - Reset the entire database
3. **Users** can:
   - Browse available books
   - View the list and details of books they've borrowed

### Navigation
- Admin and User functions are shown in separate menus after login.
- All actions are performed via GUI buttons and forms.

---

## 🧮 Fine Calculation Explained

- When returning a book, the application calculates:
   - **Days late**: If returned after the allowed period, user is charged
   - **Fine**: `₹10` for every day overdue  
   Example: If a book is issued for 7 days but returned after 9 days, fine = (9−7) × ₹10 = ₹20.


---

## Prerequisites

- **Java JDK (8+)** installed
- **MySQL Server** with access credentials
- Your preferred Java IDE (NetBeans, Eclipse, IntelliJ, etc.)
- Download JARs:  
  - `mysql-connector-j.jar` (JDBC driver)  
  - `rs2xml.jar` (DbUtils for table display)

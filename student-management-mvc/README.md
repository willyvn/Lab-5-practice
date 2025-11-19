# Student Management System -- MVC (Jakarta EE)

A complete **Student Management System** built using **JSP, Servlet,
JSTL, DAO pattern, MVC Architecture, and MySQL**.

## 📌 Features

-   Add new students\
-   Edit student information\
-   Delete students\
-   Search students (name/email)\
-   Sort by ID, Code, Name, Email, Major\
-   Clear filters\
-   Validation (student code format, email format...)\
-   MVC folder structure\
-   DAO database operations

## 📂 Project Structure

    student-management-mvc
    │── src/main/java
    │   ├── com.student.controller
    │   │     └── StudentController.java
    │   ├── com.student.dao
    │   │     └── StudentDAO.java
    │   └── com.student.model
    │         └── Student.java
    │
    │── src/main/webapp
    │   ├── views
    │   │     ├── student-list.jsp
    │   │     ├── student-form.jsp
    │   │     └── error.jsp
    │   └── WEB-INF
    │         └── web.xml

## 🗄️ Database Setup

Run in MySQL:

``` sql
CREATE DATABASE student_management;

CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_code VARCHAR(20) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    major VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## 🚀 How to Run

1.  Clone the project\
2.  Import into **Eclipse** / **IntelliJ**\
3.  Configure **Tomcat 10 / 10.1**\
4.  Update MySQL username & password in `StudentDAO.java`\
5.  Run project

## 🌐 URL Paths

  Action           URL
  ---------------- --------------------------------------
  List students    `/student?action=list`
  New student      `/student?action=new`
  Insert student   `/student?action=insert`
  Edit student     `/student?action=edit&id=...`
  Update student   `/student?action=update`
  Delete student   `/student?action=delete&id=...`
  Search           `/student?action=search&keyword=...`
  Clear Filter     `/student?action=clear`

## ✔️ Technologies Used

-   Java 17 / 11\
-   JSP / Servlet\
-   JSTL\
-   Jakarta EE\
-   MySQL\
-   JDBC\
-   Tomcat 10+

## 📘 Author

Developed by **Nguyên Phạm Minh**.

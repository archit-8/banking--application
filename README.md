# 🏦 Banking Application – Spring Boot

A simple Banking Management System built using **Spring Boot** that allows users to create accounts and perform basic banking operations such as deposit, withdrawal, and money transfer with proper validation and exception handling.

This project follows a clean layered architecture and demonstrates real-world backend development practices.

---

## 🚀 Features

- Create a new bank account
- User login
- Deposit money into account
- Withdraw money with balance validation
- Transfer money between accounts
- Global exception handling
- Error handling with custom error page
- UI built using Thymeleaf templates

---

## 🛠️ Tech Stack

- **Java**
- **Spring Boot**
- **Spring MVC**
- **Spring Data JPA**
- **Hibernate**
- **Thymeleaf**
- **H2 / SQL Database**
- **Maven**
- **Git & GitHub**

---

## 📂 Project Structure

```text
banking-application-app-assigment
│
├── src/main/java/com/example/demo
│   ├── controller        # Handles HTTP requests
│   ├── service           # Business logic layer
│   ├── repository        # Database access using JPA
│   ├── model             # Entity classes
│   ├── dto               # Request and response DTOs
│   └── exception         # Custom exceptions and handler
│
├── src/main/resources
│   ├── templates         # Thymeleaf HTML pages
│   └── application.properties
│
├── pom.xml
└── README.md





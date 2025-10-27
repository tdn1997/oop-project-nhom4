# 🧱 OOP Project - Nhóm 4

## 📦 Setup Guide

### 1. Install NetBeans (Optional)
If you prefer using an IDE for Java development:  
👉 [Download NetBeans](https://netbeans.apache.org/front/main/download/)

### 2. Install MySQL Workbench (Optional)
A GUI tool for managing your MySQL databases:  
👉 [Download MySQL Workbench](https://dev.mysql.com/downloads/workbench/)

### 3. Set Up MySQL Database

#### Option A: Install MySQL Manually  
You can download and install MySQL directly from the official documentation:  
👉 [MySQL Installation Guide](https://dev.mysql.com/doc/refman/9.1/en/installing.html)

#### Option B: Use Docker (Recommended)
Run MySQL inside a Docker container:  
```bash
# Pull the latest MySQL image
docker pull mysql:latest

# Run a new container named 'my-mysql'
docker run --name my-mysql \
  -e MYSQL_ROOT_PASSWORD=mysecretpassword \
  -p 3306:3306 \
  -d mysql:latest
```

> 💡 Tip: You can replace `mysecretpassword` with your own secure password.

### 4. Import Database Schema & Data
You can import the database in one of two ways:

#### Using MySQL Workbench:
1. Create a new database named **`oop_project_nhom4_db`**  
2. Open and execute the SQL file located at:  
   ```
   ./data/oop.sql
   ```

#### Using the Command Line:
```bash
mysql -u root -p
CREATE DATABASE oop_project_nhom4_db;
USE oop_project_nhom4_db;
SOURCE ./data/oop.sql;
```

---

## ⚙️ Additional Notes
- Make sure the database connection settings in your project match your MySQL configuration.
- Default MySQL port: **3306**
- Default username: **root**

package oop.project.nhom4.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private final String url = "jdbc:mysql://localhost:3306/oop_project_nhom4_db";
    private final String user = "root";
    private final String pass = "mysecretpassword";
    
    private Connection connection;

    public Connection getConnection() {
        try {
            this.connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connect success!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Error connection: " + e.getMessage());
        }
        return null;
    }
}
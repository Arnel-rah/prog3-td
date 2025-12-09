package jdbc.db;

import java.sql.Connection;
import java.sql.DriverAction;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private  final String URL = "jdbc:postgresql://localhost:5432/product_management_db";
    private  final String USER = "product_manager_user";
    private  final String PASSWORD = "123456";

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base : " + e.getMessage());
            return null;
        }
    }
}

package jdbc.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
       String URL = "jdbc:postgresql://localhost:5432/product_management_db";
       String USER = "product_manager_user";
       String PASSWORD = "123456";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base : " + e.getMessage());
            return null;
        }
    }
}

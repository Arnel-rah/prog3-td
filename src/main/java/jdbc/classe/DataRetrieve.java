package jdbc.classe;

import jdbc.db.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DataRetrieve {
    public static List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try(Connection con = DBConnection.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id, name FROM category")
        ) {
            while (rs.next()) {
                Category c = new Category(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                categories.add(c);
            }
            return categories;
        }
    }
}

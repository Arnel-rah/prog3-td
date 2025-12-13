package jdbc.classe;

import java.security.Timestamp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jdbc.db.DBConnection;

public class DataRetrieve {

    public  List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
              ResultSet rs = stmt.executeQuery("SELECT id, name FROM category")) {
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

    public  List<Product> getProductList(int page, int size) {
        List<Product> products = new ArrayList<>();
        try {
            Connection con = DBConnection.getConnection();

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("select * from product limit " + (page * size) + "," + size);
            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getTimestamp("creationdatetime").toInstant(),
                        (Category) rs.getObject("category")
                );
                products.add(p);
            }
            return products;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

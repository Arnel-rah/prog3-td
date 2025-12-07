package jdbc.classe;

import jdbc.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Hello {
    public static List<Category> getAllCategories() throws SQLException {
            List<Category> categories = new ArrayList<>();

            String sql = "SELECT id, name FROM product_category";

            try (Connection con = DBConnection.getConnection();
                 Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }
            }
            return categories;
        }


        /** -------------------- SIMPLE PAGINATION -------------------- */
        public static List<Product> getProductList(int page, int size) {
            List<Product> products = new ArrayList<>();

            String sql = "SELECT id, name, creation_datetime FROM product LIMIT ? OFFSET ?";

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setInt(1, size);
                stmt.setInt(2, page * size);

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getTimestamp("creation_datetime").toInstant(),
                            (Category) rs.getObject("category")
                    );
                    products.add(p);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return products;
        }

        public static List<Product> getProductsByCriteria(
                String productName,
                String categoryName,
                Instant creationMin,
                Instant creationMax,
                int page,
                int size
        ) {

            StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.name, p.creation_datetime,
                   pc.id AS cat_id, pc.name AS cat_name
            FROM product p
            LEFT JOIN product_category pc ON p.id = pc.product_id
            WHERE 1=1
        """);

            List<Object> params = new ArrayList<>();

            if (productName != null && !productName.isBlank()) {
                sql.append(" AND p.name ILIKE ? ");
                params.add("%" + productName + "%");
            }

            if (categoryName != null && !categoryName.isBlank()) {
                sql.append(" AND pc.name ILIKE ? ");
                params.add("%" + categoryName + "%");
            }
            if (creationMin != null) {
                sql.append(" AND p.creation_datetime >= ? ");
                params.add(Timestamp.from(creationMin));
            }

            if (creationMax != null) {
                sql.append(" AND p.creation_datetime <= ? ");
                params.add(Timestamp.from(creationMax));
            }

            sql.append(" ORDER BY p.id LIMIT ? OFFSET ? ");
            params.add(size);
            params.add(page * size);

            List<Product> products = new ArrayList<>();

            try (Connection con = DBConnection.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql.toString())) {

                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Product p = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getTimestamp("creation_datetime").toInstant(),
                            (Category) rs.getObject("category")
                    );

                    int catId = rs.getInt("cat_id");
                    String catName = rs.getString("cat_name");

                    if (catName != null) {
                        p.addCategory(new Category(catId, catName));
                    }

                    products.add(p);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return products;
        }
    }


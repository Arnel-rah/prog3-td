package jdbc.classe;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jdbc.db.DBConnection;

public class DataRetriever {

    private final DBConnection dbConnection;

    public DataRetriever() {
        this.dbConnection = new DBConnection();
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category";

        try (Connection con = dbConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categories.add(
                        new Category(
                                rs.getInt("id"),
                                rs.getString("name")
                        )
                );
            }
        }
        return categories;
    }

    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;

        String sql = """
            SELECT p.id, p.name, p.creation_datetime,
                   pc.id AS cat_id, pc.name AS cat_name
            FROM product p
            LEFT JOIN product_category pc ON p.id = pc.product_id
            ORDER BY p.id
            LIMIT ? OFFSET ?
        """;

        try (Connection con = dbConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, size);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Category category = null;

                    if (rs.getString("cat_name") != null) {
                        category = new Category(
                                rs.getInt("cat_id"),
                                rs.getString("cat_name")
                        );
                    }

                    products.add(
                            new Product(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getTimestamp("creation_datetime").toInstant(),
                                    category
                            )
                    );
                }
            }
        }
        return products;
    }

    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax
    ) throws SQLException {
        return getProductsByCriteria(productName, categoryName, creationMin, creationMax, 1, Integer.MAX_VALUE);
    }

    public List<Product> getProductsByCriteria(
            String productName,
            String categoryName,
            Instant creationMin,
            Instant creationMax,
            int page,
            int size
    ) throws SQLException {

        List<Product> products = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.name, p.creation_datetime,
                   pc.id AS cat_id, pc.name AS cat_name
            FROM product p
            LEFT JOIN product_category pc ON p.id = pc.product_id
            WHERE 1=1
        """);

        if (productName != null && !productName.isBlank()) {
            sql.append(" AND p.name ILIKE ? ");
            params.add("%" + productName.trim() + "%");
        }

        if (categoryName != null && !categoryName.isBlank()) {
            sql.append(" AND pc.name ILIKE ? ");
            params.add("%" + categoryName.trim() + "%");
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
        params.add((page - 1) * size);

        try (Connection con = dbConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Category category = null;

                    if (rs.getString("cat_name") != null) {
                        category = new Category(
                                rs.getInt("cat_id"),
                                rs.getString("cat_name")
                        );
                    }

                    products.add(
                            new Product(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getTimestamp("creation_datetime").toInstant(),
                                    category
                            )
                    );
                }
            }
        }
        return products;
    }
}

package jdbc.classe;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jdbc.db.DBConnection;

public class DataRetriever {

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT id, name FROM product_category";
        try (Connection con = DBConnection.getConnection()) {
            assert con != null;
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    categories.add(new Category(rs.getInt("id"), rs.getString("name")));
                }
            }
        }
        return categories;
    }

    public List<Product> getProductList(int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        int offset = (page - 1) * size;
        String sql = "SELECT p.id, p.name, p.creation_datetime, pc.id as cat_id, pc.name as cat_name " +
                "FROM product p " +
                "LEFT JOIN product_category pc ON p.id = pc.product_id " +
                "ORDER BY p.id " +
                "LIMIT ? OFFSET ?";
        try (Connection con = DBConnection.getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, size);
                pstmt.setInt(2, offset);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Instant creationDateTime = rs.getTimestamp("creation_datetime").toInstant();
                        Category category = null;
                        if (rs.getString("cat_name") != null) {
                            category = new Category(rs.getInt("cat_id"), rs.getString("cat_name"));
                        }
                        products.add(new Product(rs.getInt("id"), rs.getString("name"), creationDateTime, category));
                    }
                }
            }
        }
        return products;
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax) throws SQLException {
        return getProductsByCriteriaWithPagination(productName, categoryName, creationMin, creationMax, 1, Integer.MAX_VALUE);
    }

    public List<Product> getProductsByCriteria(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) throws SQLException {
        return getProductsByCriteriaWithPagination(productName, categoryName, creationMin, creationMax, page, size);
    }

    private List<Product> getProductsByCriteriaWithPagination(String productName, String categoryName, Instant creationMin, Instant creationMax, int page, int size) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT p.id, p.name, p.creation_datetime, pc.id as cat_id, pc.name as cat_name " +
                        "FROM product p " +
                        "LEFT JOIN product_category pc ON p.id = pc.product_id " +
                        "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (productName != null && !productName.trim().isEmpty()) {
            sqlBuilder.append("AND p.name ILIKE ? ");
            params.add("%" + productName.trim() + "%");
        }
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            sqlBuilder.append("AND pc.name ILIKE ? ");
            params.add("%" + categoryName.trim() + "%");
        }
        if (creationMin != null) {
            sqlBuilder.append("AND p.creation_datetime >= ? ");
            params.add(Timestamp.from(creationMin));
        }
        if (creationMax != null) {
            sqlBuilder.append("AND p.creation_datetime <= ? ");
            params.add(Timestamp.from(creationMax));
        }

        sqlBuilder.append("ORDER BY p.id ");

        int offset = (page - 1) * size;
        sqlBuilder.append("LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        String sql = sqlBuilder.toString();
        try (Connection con = DBConnection.getConnection()) {
            assert con != null;
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Instant creationDateTime = rs.getTimestamp("creation_datetime").toInstant();
                        Category category = null;
                        if (rs.getString("cat_name") != null) {
                            category = new Category(rs.getInt("cat_id"), rs.getString("cat_name"));
                        }
                        products.add(new Product(rs.getInt("id"), rs.getString("name"), creationDateTime, category));
                    }
                }
            }
        }
        return products;
    }
}
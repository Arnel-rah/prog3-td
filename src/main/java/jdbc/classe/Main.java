package jdbc.classe;

import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DataRetriever retriever = new DataRetriever();
        try {
            System.out.println("getAllCategories");
            List<Category> categories = retriever.getAllCategories();
            for (Category c : categories) {
                System.out.println(c);
            }
            System.out.println();

            System.out.println("getProductList int page, int size");
            System.out.println("page=1, size=10:");
            List<Product> products1_10 = retriever.getProductList(1, 10);
            for (Product p : products1_10) {
                System.out.println(p);
            }
            System.out.println("page=1, size=5:");
            List<Product> products1_5 = retriever.getProductList(1, 5);
            for (Product p : products1_5) {
                System.out.println(p);
            }
            System.out.println("page=1, size=3:");
            List<Product> products1_3 = retriever.getProductList(1, 3);
            for (Product p : products1_3) {
                System.out.println(p);
            }
            System.out.println("page=2, size=2:");
            List<Product> products2_2 = retriever.getProductList(2, 2);
            for (Product p : products2_2) {
                System.out.println(p);
            }
            System.out.println();

            System.out.println("getProductsByCriteria without pagination");
            Instant nullInstant = null;
            Instant creationMinFeb = Instant.parse("2024-02-01T00:00:00Z");
            Instant creationMaxMar = Instant.parse("2024-03-02T00:00:00Z");
            Instant creationMinJan = Instant.parse("2024-01-01T00:00:00Z");
            Instant creationMaxDec = Instant.parse("2024-12-02T00:00:00Z");

            System.out.println("\"Dell\", null, null, null:");
            List<Product> dellProducts = retriever.getProductsByCriteria("Dell", null, nullInstant, nullInstant);
            for (Product p : dellProducts) {
                System.out.println(p);
            }
            System.out.println("null, \"info\", null, null:");
            List<Product> infoProducts = retriever.getProductsByCriteria(null, "info", nullInstant, nullInstant);
            for (Product p : infoProducts) {
                System.out.println(p);
            }
            System.out.println("\"iPhone\", \"mobile\", null, null:");
            List<Product> iphoneMobileProducts = retriever.getProductsByCriteria("iPhone", "mobile", nullInstant, nullInstant);
            for (Product p : iphoneMobileProducts) {
                System.out.println(p);
            }
            System.out.println("null, null, 2024-02-01, 2024-03-01:");
            List<Product> febMarProducts = retriever.getProductsByCriteria(null, null, creationMinFeb, creationMaxMar);
            for (Product p : febMarProducts) {
                System.out.println(p);
            }
            System.out.println("\"Samsung\", \"bureau\", null, null:");
            List<Product> samsungBureauProducts = retriever.getProductsByCriteria("Samsung", "bureau", nullInstant, nullInstant);
            for (Product p : samsungBureauProducts) {
                System.out.println(p);
            }
            System.out.println("\"Sony\", \"informatique\", null, null:");
            List<Product> sonyInformatiqueProducts = retriever.getProductsByCriteria("Sony", "informatique", nullInstant, nullInstant);
            for (Product p : sonyInformatiqueProducts) {
                System.out.println(p);
            }
            System.out.println("null, \"audio\", 2024-01-01, 2024-12-01:");
            List<Product> audioJanDecProducts = retriever.getProductsByCriteria(null, "audio", creationMinJan, creationMaxDec);
            for (Product p : audioJanDecProducts) {
                System.out.println(p);
            }
            System.out.println("null, null, null, null:");
            List<Product> allProducts = retriever.getProductsByCriteria(null, null, nullInstant, nullInstant);
            for (Product p : allProducts) {
                System.out.println(p);
            }
            System.out.println();

            System.out.println("=== getProductsByCriteria (with pagination) ===");
            System.out.println("null, null, null, null, page=1, size=10:");
            List<Product> allPaginated1_10 = retriever.getProductsByCriteria(null, null, nullInstant, nullInstant, 1, 10);
            for (Product p : allPaginated1_10) {
                System.out.println(p);
            }
            System.out.println("\"Dell\", null, null, null, page=1, size=5:");
            List<Product> dellPaginated1_5 = retriever.getProductsByCriteria("Dell", null, nullInstant, nullInstant, 1, 5);
            for (Product p : dellPaginated1_5) {
                System.out.println(p);
            }
            System.out.println("null, \"informatique\", null, null, page=1, size=10:");
            List<Product> informatiquePaginated1_10 = retriever.getProductsByCriteria(null, "informatique", nullInstant, nullInstant, 1, 10);
            for (Product p : informatiquePaginated1_10) {
                System.out.println(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
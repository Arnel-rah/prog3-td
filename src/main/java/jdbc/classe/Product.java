package jdbc.classe;

import java.time.Instant;

public class Product {
    private final int id;
    private final String name;
    private final Instant Creationdatetime;
    private final Category category;

    public Product(int id, String name, Instant creationdatetime, Category category) {
        this.id = id;
        this.name = name;
        Creationdatetime = creationdatetime;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getCreationdatetime() {
        return Creationdatetime;
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryName(Category category) {
        return category.getName();
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Creationdatetime=" + Creationdatetime +
                ", category=" + category +
                '}';
    }
}

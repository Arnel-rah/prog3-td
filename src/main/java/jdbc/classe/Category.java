package jdbc.classe;

public class Category {
    private final int id;
    private final String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

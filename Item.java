
public class Item {
    private int id;

    private String name;

    private Categories category;

    private double price;

    private int quantity; 

    private int minQuantity; 

    static enum Categories {
        TECHNOLOGY,
        CANNED_FOOD,
        CLOTHES
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Item(int id, String name, Categories category, double price, int quantity, int minQuantity) {

        this.id = id;

        this.name = name;

        this.category = category;

        this.price = price;

        this.quantity = quantity;
        this.minQuantity = minQuantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public boolean belowMinQuantity() {

        return quantity <= minQuantity;

    }

    public Categories getCategory() {
        return category;

    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getMinQuantity() {
        return minQuantity;
    }

    @Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Item item = (Item) o;
    return id == item.id; // إذا تساوى الـ ID يعتبران نفس العنصر
}

@Override
public int hashCode() {
    return java.util.Objects.hash(id);
} 

    @Override
    public String toString() {
        return "\n" +
                "  id=" + id + "\n" +
                "  name=" + name + "\n" +
                "  category=" + category + "\n" +
                "  price=" + price + "\n" +
                "  quantity=" + quantity + "\n" +
                "  minQuantity=" + minQuantity + "\n";
    }
}

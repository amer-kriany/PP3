
public class Item {
    private int id;

    private String name;

    private Categories category;// فئة

    private double price;

    private int quantity;// الكمية المتوفرة

    private int minQuantity;// الحد االادنى المسموح فيه

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

    // هون مشان ترجع قيمة true/false اذا الكمية اقل رح ترجع true اذا كافيةاواكتر
    // false

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

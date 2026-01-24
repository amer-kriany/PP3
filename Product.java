import java.util.HashMap;
import java.util.Map;

public class Product {
    private int proId;
    private String proName;
    private Map<Item, Integer> former;

    // Constructor الأساسي: بدون عناصر
    public Product(int proId, String proName) {
        this.proId = proId;
        this.proName = proName;
        this.former = new HashMap<>();
    }

    // ======== Getters ========
    public int getProId() {
        return proId;
    }

    public String getProName() {
        return proName;
    }

    public Map<Item, Integer> getFormer() {
        return former;
    }

    // ======== Add Items ========
    public void addFormerByName(String itemName, int quantity) {
        Item item = Inventory.getItemByName(itemName);
        if(item != null && quantity > 0){
            former.put(item, quantity);
        } else throw new IllegalArgumentException(
            "Item '" + itemName + "' does not exist or quantity is invalid"
        );
    }

    public void addFormerById(int itemId, int quantity) {
        Item item = Inventory.getItemById(itemId);
        if(item != null && quantity > 0){
            former.put(item, quantity);
        } else throw new IllegalArgumentException(
            "Item ID '" + itemId + "' does not exist or quantity is invalid"
        );
    }

    // ======== Show Items ========
    public void showItemsOfProduct() {
        System.out.println("\nProduct name: " + proName);
        if(!former.isEmpty()){
            System.out.println("Required items:");
            for(var entry : former.entrySet()){
                Item item = entry.getKey();
                int quantity = entry.getValue();
                System.out.println("Item: " + item.getName() + " | Required: " + quantity);
            }
            System.out.println("_____");
        } else {
            System.out.println("No items assigned to this product!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Product)) return false;
        Product p = (Product) o;
        return proId == p.proId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(proId);
    }

    @Override
    public String toString() {
        return this.proName; // اسم المنتج فقط
    }
}


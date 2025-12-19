import java.util.Map;

class Recipe {
    private String productName;
    private Map<Item, Integer> requiredItems;

    Recipe(String productName, Map<Item, Integer> requiredItems) {
        this.productName = productName;
        this.requiredItems = requiredItems;
    }

    // getters
    public String getProductName() {
        return productName;
    }

    public Map<Item, Integer> getRequiredItem() {
        return requiredItems;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "product='" + productName + '\'' +
                ", items=" + requiredItems +
                '}';

    }
}
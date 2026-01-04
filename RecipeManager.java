import java.util.Map;
import java.util.HashMap;

public class RecipeManager {

    private static final Map<String, Recipe> recipes = new HashMap<>();

    // to load firstly
    public static void loadRecipes() {
        // Laptop
        Map<Item, Integer> laptopItems = new HashMap<>();
        laptopItems.put(Inventory.getItemByName("CPU"), 1);
        laptopItems.put(Inventory.getItemByName("RAM"), 2);
        laptopItems.put(Inventory.getItemByName("SSD"), 1);
        recipes.put("Laptop", new Recipe("Laptop", laptopItems));

        // Phone
        Map<Item, Integer> phoneItems = new HashMap<>();
        phoneItems.put(Inventory.getItemByName("CPU"), 1);
        phoneItems.put(Inventory.getItemByName("RAM"), 1);
        phoneItems.put(Inventory.getItemByName("Screen"), 1);
        recipes.put("Phone", new Recipe("Phone", phoneItems));

        // Tablet
        Map<Item, Integer> tabletItems = new HashMap<>();
        tabletItems.put(Inventory.getItemByName("CPU"), 1);
        tabletItems.put(Inventory.getItemByName("RAM"), 2);
        tabletItems.put(Inventory.getItemByName("Screen"), 1);
        recipes.put("Tablet", new Recipe("Tablet", tabletItems));
    }

    public static Recipe getRecipe(String productName) {
        return recipes.get(productName);
    }

}
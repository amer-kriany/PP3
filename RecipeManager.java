import java.util.Map;
import java.util.HashMap;
public class RecipeManager {

    private static final Map<String, Recipe> recipes = new HashMap<>();
     static{
        Map<Item, Integer> tabletItems = new HashMap<>();
        tabletItems.put(Inventory.getItemByName("Aerosil"), 2);
        tabletItems.put(Inventory.getItemByName("PVP"), 3);
        tabletItems.put(Inventory.getItemByName("LAC"), 4);

        recipes.put("Tablet", new Recipe("Tablet", tabletItems));
         Map<Item,Integer> syrupItems = new HashMap<>();
         syrupItems.put(Inventory.getItemByName("sugar"),5);
         recipes.put("Syrub",new Recipe("Syrub", syrupItems));
    }

    public static Recipe getRecipe(String productName) {
        return recipes.get(productName);
    }
}

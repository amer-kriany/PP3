import java.util.Map;
import java.util.HashMap;
public class RecipeManager {

    private static final Map<String, Recipe> recipes = new HashMap<>();
     static{
        Map<Item, Integer> Tablet = new HashMap<>();
        Tablet.put(Inventory.getItemByName("PVP"), 3);
        Tablet.put(Inventory.getItemByName("LAC"), 4);
        Tablet.put(Inventory.getItemByName("Aerosil"), 2);

        recipes.put("Tablet", new Recipe("Tablet", Tablet));
         Map<Item,Integer> Syrub = new HashMap<>();
         Syrub.put(Inventory.getItemByName("Sugar"),5);
         recipes.put("Syrub",new Recipe("Syrub", Syrub));
    }

    public static Recipe getRecipe(String productName) {
        return recipes.get(productName);
    }
}

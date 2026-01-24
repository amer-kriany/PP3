import java.util.Map;
import java.util.HashMap;
public class RecipeManager {

    private static final Map<String, Recipe> recipes = new HashMap<>();
    // to load firstly
     static{
       
               Map<Item, Integer> laptopItems = new HashMap<>();
        laptopItems.put(Inventory.getItemByName("CPU"), 1);
        laptopItems.put(Inventory.getItemByName("RAM8"), 4);
        laptopItems.put(Inventory.getItemByName("SSD128"), 4);
        laptopItems.put(Inventory.getItemByName("Screen"), 1);
        recipes.put("Laptop", new Recipe("Laptop", laptopItems));

        // Phone
        Map<Item, Integer> phoneItems = new HashMap<>();
        phoneItems.put(Inventory.getItemByName("CPU"), 1);
        phoneItems.put(Inventory.getItemByName("RAM8"), 1);
        phoneItems.put(Inventory.getItemByName("Screen"), 1);
        phoneItems.put(Inventory.getItemByName("SSD128"), 1);
        recipes.put("Phone", new Recipe("Phone", phoneItems));

        // Tablet
        Map<Item, Integer> tabletItems = new HashMap<>();
        tabletItems.put(Inventory.getItemByName("CPU"), 1);
        tabletItems.put(Inventory.getItemByName("RAM8"), 2);
        tabletItems.put(Inventory.getItemByName("SSD128"), 2);
        tabletItems.put(Inventory.getItemByName("Screen"), 1);
        recipes.put("Tablet", new Recipe("Tablet", tabletItems));

        Map<Item, Integer> HOODIEItems = new HashMap<>();
        HOODIEItems.put(Inventory.getItemByName("cotton"), 1);
        HOODIEItems.put(Inventory.getItemByName("wool"), 3);
        HOODIEItems.put(Inventory.getItemByName("polyster"), 1);
        recipes.put("HOODIE", new Recipe("HOODIE", HOODIEItems));

        Map<Item, Integer> JEANSEItems = new HashMap<>();
        JEANSEItems.put(Inventory.getItemByName("cotton"), 2);
        JEANSEItems.put(Inventory.getItemByName("polyster"), 1);
        recipes.put("JEANSE", new Recipe("JEANSE", JEANSEItems));

        Map<Item, Integer> JACKETItems = new HashMap<>();
        JACKETItems.put(Inventory.getItemByName("cotton"), 3);
        JACKETItems.put(Inventory.getItemByName("wool"), 2);
        JACKETItems.put(Inventory.getItemByName("polyster"), 1);
        recipes.put("JACKET", new Recipe("JACKET", JACKETItems));

         Map<Item, Integer> cansOfTuna = new HashMap<>();
        cansOfTuna.put(Inventory.getItemByName("Fishtuna"), 1);
        cansOfTuna.put(Inventory.getItemByName("cans"), 1);
        recipes.put("tuna", new Recipe("tuna", cansOfTuna));

         Map<Item, Integer> cansOfsardine = new HashMap<>();
        cansOfsardine.put(Inventory.getItemByName("Fishsardine"), 3);
        cansOfsardine.put(Inventory.getItemByName("cans"), 1);
        recipes.put("sardines", new Recipe("sardines", cansOfsardine));


         Map<Item, Integer> canOFLanchun = new HashMap<>();
        canOFLanchun.put(Inventory.getItemByName("chicken"), 1);
        canOFLanchun.put(Inventory.getItemByName("cans"), 1);
        recipes.put("Lanchun", new Recipe("Lanchun", canOFLanchun));



         
    }
    


    


    public static Recipe getRecipe(String productName) {
        return recipes.get(productName);
    }
}

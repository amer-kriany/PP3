import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private static final Map< Item,Integer> stock = new HashMap<>();

    public static void addItem(Item item , int qty) {
        stock.put(item, stock.getOrDefault(item, 0)+qty);
        // System.out.println("the item has been added to the inventory successfully!");
    }

    public static Item getItemByName(String name) {
        for (Item item : stock.keySet()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static Item getItemById(int id) {
        for (Item item : stock.keySet()) {
            if (item.getId()==id) {
                return item;
            }
        }
        return null;
    }


    // public static boolean hasItem(Item item) {
    //     return stock.containsKey(item.getId());
    // }

    // public static void clear() {
    //     stock.clear();
    // }
    public static Boolean hasEnough(Recipe recipe, int taskqty){
        for(Map.Entry<Item,Integer> e : recipe.getRequiredItem().entrySet()){
            int needed=e.getValue()*taskqty;
            if(stock.getOrDefault(e.getKey(),0)<needed)return false;
        }
     return true;
}
    public static void consume(Recipe recipe , int taskqty){
    if(!hasEnough(recipe, taskqty)){
        throw new IllegalStateException("Not enough items in inventory");
    }
        for(Map.Entry<Item,Integer> e : recipe.getRequiredItem().entrySet()){
            int needed= e.getValue() * taskqty;
            stock.replace(e.getKey(), stock.get(e.getKey())-needed);
        }
    }
    
}


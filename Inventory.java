import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private static final Map<Integer, Item> items = new HashMap<>();

    public static void addItem(Item item) {
        items.put(item.getId(), item);
        System.out.println("the item has been added to the inventory successfully!");
    }

    public static Item getItemByName(String name) {
        for (Item item : items.values()) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    public static Item getItemById(int id) {
        return items.get(id);
    }

    public static boolean hasItem(Item item) {
        return items.containsKey(item.getId());
    }

    public static void clear() {
        items.clear();
    }
}


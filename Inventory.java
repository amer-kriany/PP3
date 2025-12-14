import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<Integer,Item>items=new HashMap<>();
    public  void addItem(Item item){
        items.put(item.getId(),item);
        System.out.println("the item has been added to the inventory successfully!");
    }
    public Item getItemByName(String name){
        for(Item item:items.values()){
            if(item.getName().equalsIgnoreCase(name)){
                return item;
            }
        }
        return null;
    }
    public Item getItemById(int id){
        for(Item item:items.values()){
            if(item.getId()==id){
                return item;
            }
        }
        return  null;
    }

  //  public boolean hasItem(Item item){
   //  return items.containsKey(item.getId());

    }


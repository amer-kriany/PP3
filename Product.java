import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
   private int proId;
    private String proName;
    private Map<Item,Integer> former;
    private Inventory inventory;

    public int getProId() {
        return proId;
    }

    public String getProName() {
        return proName;
    }

    public Map<Item, Integer> getFormer() {
        return former;
    }

    public Product(int proId, String proName) {
        this.proId = proId;
        this.proName = proName;
        this.former= new HashMap<>();
        
    }
    public void addFormerByName(String itemName,int quantity){
        Item item=inventory.getItemByName(itemName);
        if(item !=null&&quantity>0){
            former.put(item,quantity);
        }else throw new IllegalArgumentException("the item does not exists or quantity entry error");
    }
    public void addFormerById(int itemId,int quantity){
        Item item=inventory.getItemById(itemId);
        if(item !=null&&quantity>0){
            former.put(item,quantity);
        }else throw new IllegalArgumentException("the item does not exists or quantity entry error");
    }
public void showItemsOfProduct(){
    System.out.println(" ");
    System.out.println("Product name : "+proName);
    if(!former.isEmpty()){
        System.out.println("Required items : ");
        for(var entry : former.entrySet()){//var instead of Map.Entry<Item,Integer>entry
            Item item=entry.getKey();
            int quantity=entry.getValue();
            System.out.print("Item: "+item.getName());
            System.out.println(" Required "+quantity+" of quantity.");
            System.out.println(" ");
        }
        System.out.println("_____");
    }else
        System.out.println("No items assigned to this product!");
}


    /*List<Item>inventory=new ArrayList<>();
    public void addProduct(int itemId,int quantity){
        for(Item item:inventory){
            if(item.getId()==itemId){
                former.put(item,quantity);
                System.out.println("the former " +item.getName()+" has been added successfully");
                return;
            }else{
                System.out.println("The item is not in the inventory");
            }

        }
    } it didn't work I don't know why....*/







}
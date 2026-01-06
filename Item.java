public class Item  {

    private int id;
      private String name;
       private String category;
     private double price;
         private int quantity;//الكمية المتوفرة
    private int minQuantity;//الحد االادنى المسموح فيه
    

     public static final String MEDICINE="MEDICINE";
    
     public static final String MEDICAL_DEVICES="MEDICAL_DEVICES";

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
    this.quantity = quantity;
}

    public int getMinQuantity() {
        return minQuantity;
    }


    public Item(int id, String name, String category, double price, int quantity, int minQuantity) {

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.minQuantity = minQuantity;
    }


    // هون مشان ترجع قيمة true/false اذا الكمية اقل رح ترجع true اذا كافيةاواكتر false

    public boolean belowMinQuantity() {

          return quantity <= minQuantity;

    }


@Override
public String toString() {
    return "\n" +
             "  id:" + id + "\n" +
           "  name:" + name + "\n" +
           "  category:" + category + "\n" +
           "  price:" + price + "\n" +
           "  quantity:" + quantity + "\n" +
           "  minQuantity:" + minQuantity + "\n" 
           ;
}
}
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class main {
    public static void main(String[] args) {
        
        //Item
        Item item1=new Item(101,"Aerosil",Item.MEDICINE,5000,120,60);
        Item item2=new Item(102,"Paracetamol",Item.MEDICINE,3000,200,150);
        Item item3=new Item(103,"PVP",Item.MEDICINE,2200,130,90);
        Item item4=new Item(104,"Strach",Item.MEDICINE,6600,120,60);
        Item item5=new Item(105,"LAC",Item.MEDICINE,4100,120,60);
        Item item6=new Item(106,"Mg Stearate",Item.MEDICINE,8200,120,60);

        Item item7=new Item(107,"Amoxicillin Trihydrate",Item.MEDICINE,900,220,190);
        Item item8=new Item(108,"Cellulose",Item.MEDICINE,2230,80,50);
        Item item9=new Item(109,"Titanium Dioxide",Item.MEDICINE,1100,120,60);
        Item item10=new Item(110,"HPMC",Item.MEDICINE,2350,420,400);
        Item item11=new Item(111,"CPVP",Item.MEDICINE,3475,275,215);
        Item item12=new Item(112,"MCC",Item.MEDICINE,5520,520,450);
        Item item13=new Item(113,"Acetylasalicylic Acid",Item.MEDICINE,10000,6000,4000);
        Item sugar =new Item(100, "Sugar", Item.MEDICAL_DEVICES , 1000 , 100 , 50);
       
        
        //Inventory

        

        Inventory.addItem(item2 , 200);
        Inventory.addItem(item3 , 130);
        Inventory.addItem(item1 , 120);
        Inventory.addItem(item4 , 120);
        Inventory.addItem(item5 , 120);
        Inventory.addItem(item6 , 120);
        Inventory.addItem(item7 , 220);
        Inventory.addItem(item8 , 80);
        Inventory.addItem(item9 , 120);
        Inventory.addItem(item10 , 420);
        Inventory.addItem(item11 , 275);
        Inventory.addItem(item12 , 520);
        Inventory.addItem(item13 , 6000);
        Inventory.addItem(sugar, 100);
        

        //productLines
        ProductLine tabletLine=new ProductLine(0, "Tablet", ProductLine.State.ACTIVE);
        ProductLine syrubLine=new ProductLine(1, "Syrub", ProductLine.State.ACTIVE);
         ArrayList<ProductLine> arr= new ArrayList<>();
         arr.add(tabletLine);
         arr.add(syrubLine);


         System.out.println("Before: " + Inventory.getStock().get(sugar));

 // tasks
         Task task1=new Task("moha", "Syrub", 10, "23-12-2025 02:00:00");
         Task task2=new Task("amer", "Tablet", 5, "23-12-2025 02:00:00");
        
         //producctionManager
         ProductionManager PM = new ProductionManager(arr);
         PM.addTask(task1, syrubLine.getLineName());
         PM.addTask(task2, tabletLine.getLineName());
            System.out.println("Task accepted");
            syrubLine.start();
            tabletLine.start();
       

System.out.println("After: " + Inventory.getStock().get(sugar));


        //=======================================


    }
}

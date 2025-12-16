import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
public class main {
    public static void main(String[] args) {
        // Task t=new Task();
        // Scanner input=new Scanner(System.in);
        // System.out.print("Enter your name: ");
        // String clientName=input.nextLine();
        // System.out.print("Enter the desire Product : ");
        // String desireProduct=input.nextLine();
        // System.out.print("Enter the quantity : ");
        // int quantity=input.nextInt();
        // input.nextLine();

        // while(t.deadLine==null){
        // System.out.print("Enter the DeadLine: (dd-MM-yyyy HH:mm:ss): ");
        // String deadLine=input.nextLine();
        // t.setDeadLine(deadLine);
        // }
 //  System.out.printf("Task created for client: %s \n Task Number is: %04d %n", task.getClientName(), task.taskID);


        //productLines
        ProductLine p=new ProductLine(0, "Tablet", "going");
        ProductLine p1=new ProductLine(1, "Syrub", "going");
         ArrayList<ProductLine> arr= new ArrayList<>();
         arr.add(p);
         arr.add(p1);

         // tasks
         Task task1=new Task("moha", "Syrup", 150, "23-12-2025 02:00:00");
        
         //producctionManager
         ProductionManager PM = new ProductionManager(arr);
         PM.addTask(task1, p.getLineName());
       
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

        //Inventory

        Inventory inventory=new Inventory();

        inventory.addItem(item1 , 120);
        inventory.addItem(item2 , 200);
        inventory.addItem(item3 , 130);
        inventory.addItem(item4 , 120);
        inventory.addItem(item5 , 120);
        inventory.addItem(item6 , 120);
        inventory.addItem(item7 , 220);
        inventory.addItem(item8 , 80);
        inventory.addItem(item9 , 120);
        inventory.addItem(item10 , 420);
        inventory.addItem(item11 , 275);
        inventory.addItem(item12 , 520);
        inventory.addItem(item13 , 6000);

        //=======================================


    }
}

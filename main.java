import java.util.Scanner;
import java.util.ArrayList;
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
        ProductLine p=new ProductLine(0, "metal", "going");
        ProductLine p1=new ProductLine(1, "cotton", "going");
        ProductLine p2=new ProductLine(2, "feather", "going");

         Task task=new Task("amer", "feather", 100,"20-12-2025 12:00:00");
         Task task1=new Task("moha", "metal", 150, "23-12-2025 02:00:00");
         ArrayList<ProductLine> arr= new ArrayList<>();
         arr.add(p);
         arr.add(p1);
         arr.add(p2);
         ProductionManager PM = new ProductionManager(arr);
         PM.addTask(task, p2.getLineName());
         PM.addTask(task1, p.getLineName());
         System.out.println(arr.get(0).productLineTasks.toString());
        //  System.out.printf("Task created for client: %s \n Task Number is: %04d %n", task.getClientName(), task.taskID);

        //   Item item1=new Item(101,"Aerosil",Item.MEDICINE,5000,120,60);
        // Item item2=new Item(102,"Paracetamol",Item.MEDICINE,3000,200,150);
        // Item item3=new Item(103,"PVP",Item.MEDICINE,2200,130,90);
        // Item item4=new Item(104,"Strach",Item.MEDICINE,6600,120,60);
        // Item item5=new Item(105,"LAC",Item.MEDICINE,4100,120,60);
        // Item item6=new Item(106,"Mg Stearate",Item.MEDICINE,8200,120,60);

        // Item item7=new Item(107,"Amoxicillin Trihydrate",Item.MEDICINE,900,220,190);
        // Item item8=new Item(108,"Cellulose",Item.MEDICINE,2230,80,50);
        // Item item9=new Item(109,"Titanium Dioxide",Item.MEDICINE,1100,120,60);
        // Item item10=new Item(110,"HPMC",Item.MEDICINE,2350,420,400);
        // Item item11=new Item(111,"CPVP",Item.MEDICINE,3475,275,215);
        // Item item12=new Item(112,"MCC",Item.MEDICINE,5520,520,450);
        // Item item13=new Item(113,"Acetylasalicylic Acid",Item.MEDICINE,10000,6000,4000);
        //         Inventory inventory=new Inventory();

        //   inventory.addItem(item1);
        // inventory.addItem(item2);
        // inventory.addItem(item3);
        // inventory.addItem(item4);
        // inventory.addItem(item5);
        // inventory.addItem(item6);
        // inventory.addItem(item7);
        // inventory.addItem(item8);
        // inventory.addItem(item9);
        // inventory.addItem(item10);
        // inventory.addItem(item11);
        // inventory.addItem(item12);
        // inventory.addItem(item13);


    }
}

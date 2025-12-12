import java.util.Scanner;
public class main {
    public static void main(String[] args) {
        Tasks t=new Tasks();
        Scanner input=new Scanner(System.in);
        System.out.print("Enter your name: ");
        String clientName=input.nextLine();
        System.out.print("Enter the desire Product : ");
        String desireProduct=input.nextLine();
        System.out.print("Enter the quantity : ");
        int quantity=input.nextInt();
        input.nextLine();

        while(t.deadLine==null){
        System.out.print("Enter the DeadLine: (dd-MM-yyyy HH:mm:ss): ");
        String deadLine=input.nextLine();
        t.deadLine(deadLine);
        }
         Tasks task=new Tasks(clientName, desireProduct, quantity);
         System.out.printf("Task created for client: %s \n Task Number is: %04d %n", task.clientName, Tasks.taskNumber);
    }
}

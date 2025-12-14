import java.util.*;

public class ProductLine extends Thread {
    public final int lineId;
    public final String lineName;
    private String state;
    public final ArrayList<Task> tasks;

    // قائمة كل خطوط الإنتاج
    public static final ArrayList<ProductLine> allLines = new ArrayList<>();

    public ProductLine(int lineId, String lineName, String state, List<Task> tasks) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.state = state;
        this.tasks = new ArrayList<>(tasks);
        synchronized (allLines) {
            allLines.add(this);
        }
    }

    // public static ProductLine addLine(int lineId, String lineName, String state,
    // List<String> tasks) {
    // return new ProductLine(lineId, lineName, state, tasks);
    // }

    // public static void addLineFromUser(Scanner input) {

    // try {
    // System.out.print("Enter line id: ");
    // int id = input.nextInt();
    // input.nextLine(); // تنظيف السطر

    // System.out.print("Enter line name: ");
    // String name = input.nextLine();

    // System.out.print("Enter line state (active / stop / maintenance): ");
    // String state = input.nextLine();

    // System.out.print("Enter number of tasks: ");
    // int taskCount = input.nextInt();
    // input.nextLine();

    // ArrayList<String> tasks = new ArrayList<>();
    // for (int i = 0; i < taskCount; i++) {
    // System.out.print("Enter task " + (i + 1) + ": ");
    // tasks.add(input.nextLine());
    // }

    // // إضافة الخط
    // ProductLine.addLine(id, name, state, tasks);

    // System.out.println("✔ Line added successfully.\n");

    // } catch (Exception e) {
    // System.out.println("❌ Invalid input: " + e.getMessage());
    // input.nextLine(); // تنظيف
    // }
    // }

    public void setState(String newState) {
    this.state = newState;
    }

    @Override
    public void run() {

        try {
            if (state.equals("active")) {
                
                System.out.println("Line " + lineId + " (" + lineName + ") tasks: " + tasks);
                Thread.sleep(5000); // تأخير اختياري
            }

            else if (state.equals("stop")) {
                System.out.println("Line " + lineId + " is STOPPED.");
                Thread.sleep(5000);
                return;
            }

            else if (state.equals("maintenance")) {
                System.out.println("Line " + lineId + " is under MAINTENANCE.");
                Thread.sleep(5000);
                return;
            } else {
                throw new IllegalArgumentException(
                        "Invalid state for line " + lineId + ". Allowed states: active, stop, maintenance");
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IllegalArgumentException e) {
            // إذا حالة غلط
            System.out.println("Error: " + e.getMessage());
        }
    }
}
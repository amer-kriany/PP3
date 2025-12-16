import java.util.*;

public class ProductLine extends Thread {
    public int lineId;
    private String lineName;

    enum State {
        ACTIVE, STOP, MAINTENANCE;
    }

    State state;

    public ArrayList<Task> productLineTasks;

    public ProductLine(int lineId, String lineName, State state, List<Task> productLineTasks) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.state = state;
        this.productLineTasks = new ArrayList<>(productLineTasks);

    }

    public String getLineName() {
        return lineName;
    }

    public void addTask(Task task) {
        productLineTasks.add(task);
    }

    public ArrayList<Task> getProductLineTasks() {
        return productLineTasks;
    }

    public static ProductLine addLine(int lineId, String lineName, State state) {
        return new ProductLine(lineId, lineName, state, new ArrayList<>());
    }

    public static void addLineFromUser() {
        Scanner input = new Scanner(System.in);
        State state = null;
        int num = 1;
        try {
            System.out.print("Enter line id: ");
            int id = input.nextInt();
            input.nextLine();

            System.out.print("Enter line name: ");
            String name = input.nextLine();
            System.out.println("Enter 1 to Active\n" +
                    "Enter 2 to STOP\n" +
                    "Enter 3 to Maintenance");

            while (state == null) {
                System.out.println("Enter your choice: ");
                num = input.nextInt();
                switch (num) {

                    case 1:
                        state = State.ACTIVE;
                        break;
                    case 2:
                        state = State.STOP;
                        break;
                    case 3:
                        state = State.MAINTENANCE;
                        break;

                    default:
                        System.out.println("try agin");
                        break;

                }
            }

            ProductLine.addLine(id, name, state);
            System.out.println("Line added successfully.\n");

        } catch (Exception e) {
            System.out.println("Invalid input: " + e);
            input.nextLine();
        }
    }

    public void setState(State newState) {
        this.state = newState;
    }

    @Override
    public void run() {

        try {
            if (state.equals(State.ACTIVE)) {

                System.out.println("Line " + lineId + " (" + lineName + ") tasks: " + productLineTasks);
                Thread.sleep(5000); // تأخير اختياري
            }

            else if (state.equals(State.STOP)) {
                System.out.println("Line " + lineId + " is STOPPED.");
                Thread.sleep(5000);
                return;
            }

            else if (state.equals(State.MAINTENANCE)) {
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
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static Task addTask(String string, String string2, int i) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addTask'");
    }
}
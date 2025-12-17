import java.util.*;

public class ProductLine extends Thread {
    public int lineId;
    private String lineName;

    enum State {
        ACTIVE, STOP, MAINTENANCE;
    }

    State state;

    public ArrayList<Task> productLineTasks;

    public ProductLine(int lineId, String lineName, State state) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.state = state;
        this.productLineTasks = new ArrayList<>();

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
        List<Task> defult = new ArrayList<>();
        return new ProductLine(lineId, lineName, state);
    }

    public void setState(State newState) {
        this.state = newState;
    }

    @Override
    public void run() {

        try {
            if (state.equals(State.ACTIVE)) {
                System.out.println("Line " + lineId + " (" + lineName + ") tasks: " + productLineTasks);
                Thread.sleep(5000);
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
            System.out.println("Error: in Thread");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
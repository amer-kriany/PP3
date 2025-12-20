import java.util.*;

public class ProductLine extends Thread {
    private int lineId;
    private String lineName;

    enum State {
        ACTIVE, STOP, MAINTENANCE;
    }

    State state;

    public List<Task> productLineTasks;

    public ProductLine(int lineId, String lineName, State state, List<Task> productLineTasks) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.state = state;
        this.productLineTasks = Collections.synchronizedList(new ArrayList<>(productLineTasks));
    }

    public String getLineName() {
        return lineName;
    }

    public int getLineId() {
        return lineId;
    }

    public void addTask(Task task) {
        productLineTasks.add(task);
    }

    public List<Task> getProductLineTasks() {
        return productLineTasks;
    }

    public static ProductLine addLine(int lineId, String lineName, State state) {
        List<Task> defult = new ArrayList<>();
        return new ProductLine(lineId, lineName, state, defult);
    }

    public void setState(State newState) {
        this.state = newState;
    }

    @Override
    public void run() {
        if (state == State.ACTIVE) {
            System.out.println("Line " + lineId + " (" + lineName + ") starting tasks...");

            synchronized (productLineTasks) {
                for (Task task : productLineTasks) {
                    new Thread(() -> {
                        synchronized (Inventory.class) {
                            try {
                                Recipe recipe = RecipeManager.getRecipe(task.getDesireProduct());
                                if (Inventory.hasEnough(recipe, task.getQuantity())) {
                                    Inventory.consume(recipe, task.getQuantity());
                                    task.start();
                                    task.complete();
                                    System.out.println("Task " + task.taskID + " completed successfully.");
                                } else {
                                    System.out.println("Not enough items in inventory for Task " + task.taskID);
                                }
                            } catch (Exception e) {
                                System.out.println("Error in Task " + task.taskID + ": " + e.getMessage());
                            }
                        }
                    }).start();
                }
            }

        } else if (state == State.STOP) {
            System.out.println("Line " + lineId + " is STOPPED.");
        } else if (state == State.MAINTENANCE) {
            System.out.println("Line " + lineId + " is under MAINTENANCE.");
        }
    }
}
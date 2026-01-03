import java.util.*;

public class ProductLine extends Thread {
    private int lineId;
    private String lineName;

    enum State {
        ACTIVE, STOP, MAINTENANCE;
    }

    State state;

    public List<Task> productLineTasks;

    public ProductLine(int lineId, String lineName, State state) {
        this.lineId = lineId;
        this.lineName = lineName;
        this.state = state;
         this.productLineTasks = Collections.synchronizedList(new ArrayList<>());
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
    public void cancelTask(Task task){
           boolean removed= productLineTasks.remove(task);
            if(!removed){
                throw new NoSuchElementException("task not found");
            }
        
    }

    public List<Task> getProductLineTasks() {
        return productLineTasks;
    }

    public static ProductLine addLine(int lineId, String lineName, State state) {
        return new ProductLine(lineId, lineName, state);
    }

    public void setState(State newState) {
        this.state = newState;
    }

    private void executeTask(Task task) throws Exception {
        synchronized (Inventory.class) {
            Recipe recipe = RecipeManager.getRecipe(task.getDesireProduct());
            if (recipe == null) {
                throw new Exception("No recipe found for product: " + task.getDesireProduct());
            }

            if (!Inventory.hasEnough(recipe, task.getQuantity())) {
                task.cancel();
                throw new Exception("Not enough inventory for Task " + task.taskID);
            }

            Inventory.consume(recipe, task.getQuantity());
            task.start();

            for (int i = 1; i <= task.getQuantity(); i++) {
                task.updateProductionProgressPercentege((i * 100) / task.getQuantity());
                System.out.println("Task " + task.taskID + " progress: " + task.getProductionProgressPercentege() + "%");
                Thread.sleep(50); // يعطي تأثير واقعي للتقدم
            }

            task.complete();
            System.out.println("Task " + task.taskID + " completed successfully.");
        }
    }

    // =================== run() ===================
    @Override
    public void run() {
        while (state == State.ACTIVE) {
            synchronized (productLineTasks) {
                for (Task task : productLineTasks) {
                    if (task.getStatus() == Task.TaskStatus.PENDING) {
                        new Thread(() -> {
                            try {
                                executeTask(task);
                            } catch (Exception e) {
                                // ترمي للـ console ويمكن للـ caller التعامل معها
                                System.err.println("Error executing Task " + task.taskID + ": " + e.getMessage());
                            }
                        }).start();
                    }
                }
            }

            try {
                Thread.sleep(1000); // كل ثانية يتأكد من أي task جديدة
            } catch (InterruptedException e) {
                System.err.println("Line thread interrupted: " + e.getMessage());
            }
        }

        // بعد ما يخرج من loop
        if (state == State.STOP) {
            System.out.println("Line " + lineId + " is STOPPED.");
        }
        if (state == State.MAINTENANCE) {
            System.out.println("Line " + lineId + " is under MAINTENANCE.");
        }
    }
}
    


    
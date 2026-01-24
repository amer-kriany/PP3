import java.util.*;

public class ProductLine extends Thread {
    private int lineId;
    private String lineName;
    private volatile State state;
    private Object PauseLock = new Object();

    enum State {
        ACTIVE, STOP, MAINTENANCE;
    }

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

    public List<Task> getProductLineTasks() {
        return productLineTasks;
    }
     public void cancelTask(Task task){
           boolean removed= productLineTasks.remove(task);
            if(!removed){
                throw new NoSuchElementException("task not found");
            }
        
    }

    public void setState(State newState) {
        this.state = newState;
        if (newState == State.ACTIVE) {
            synchronized (PauseLock) {
                PauseLock.notifyAll();
            }
        }
    }

    public int getLinePerformance() {
        List<Task> tasks = getProductLineTasks();
        if (tasks == null || tasks.isEmpty())
            return 0;

        double totalRequired = 0;
        double totalProduced = 0;

        for (Task task : tasks) {
            totalRequired += task.getQuantity();
            totalProduced += (task.getQuantity() * task.getProductionProgressPercentege()) / 100.0;
        }

        if (totalRequired == 0)
            return 0;

        return (int) Math.round((totalProduced / totalRequired) * 100);
    }

    private void executeTask(Task task) throws Exception {
        Recipe recipe = RecipeManager.getRecipe(task.getDesireProduct());
        if (recipe == null)
            throw new IllegalArgumentException("No recipe found for product: " + task.getDesireProduct());

        synchronized (Inventory.class) {
            if (!Inventory.hasEnough(recipe, task.getQuantity())) {
                throw new IllegalStateException("Not enough inventory for Task " + task.taskID);
            }
            Inventory.consume(recipe, task.getQuantity());
        }

        task.start();

        for (int i = task.getProductionProgressPercentege(); i < task.getQuantity(); i++) {
            synchronized (PauseLock) {
                while (state != State.ACTIVE) {
                    PauseLock.wait();
                }
            }

            task.updateProductionProgressPercentege((i + 1) * 100 / task.getQuantity());
            System.out.println("Task " + task.taskID + " progress: " + task.getProductionProgressPercentege() + "%");
            Thread.sleep(5000);
        }

        task.complete();
        System.out.println("Task " + task.taskID + " completed successfully.");
    }

    @Override
    public void run() {
        while (state == State.ACTIVE) {
            synchronized (productLineTasks) {
                for (Task task : productLineTasks) {
                    if (task.getStatus() == Status.taskStatus.PENDING ||
                            (task.getStatus() == Status.taskStatus.IN_PROGRESS &&
                                    task.getProductionProgressPercentege() < task.getQuantity())) {
                        new Thread(() -> {
                            try {
                                executeTask(task);
                            } catch (Exception e) {
                                FileManager
                                        .logError("Line " + lineId + " | Task " + task.taskID + " | " + e.getMessage());
                                task.cancel();
                            }
                        }).start();
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                FileManager.logError("Line :" + lineId + "interrupted :" + e.getMessage());
            }
        }

        if (state == State.STOP) {
            System.out.println("Line " + lineId + " is STOPPED.");
        }
        if (state == State.MAINTENANCE) {
            System.out.println("Line " + lineId + " is under MAINTENANCE.");
        }
    }
}
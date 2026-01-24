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

    public void cancelTask(Task task) {
        boolean removed = productLineTasks.remove(task);
        if (!removed) {
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
        int totalProduced = 0;
        for (Task task : getProductLineTasks()) {
            totalProduced += task.getProductionProgress();
        }
        return totalProduced;
    }

    private void executeTask(Task task) throws Exception {
        Recipe recipe = RecipeManager.getRecipe(task.getProduct().toString());
        if (recipe == null)
            throw new IllegalArgumentException(
                    "No recipe found for product: " + task.getProduct());

        synchronized (Inventory.class) {
            if (!Inventory.hasEnough(recipe, task.getQuantity())) {
                throw new IllegalStateException(
                        "Not enough inventory for Task " + task.taskID);
            }
            Inventory.consume(recipe, task.getQuantity());
        }

        task.start();

        while (task.getProductionProgress() < task.getQuantity()) {
            synchronized (PauseLock) {
                while (state != State.ACTIVE) {
                    PauseLock.wait();
                }
            }

            task.updateProductionProgress(1); // خطوة واحدة

            Thread.sleep(5000);
        }

        task.complete();
    }

    @Override
    public void run() {
        while (state == State.ACTIVE) {
            synchronized (productLineTasks) {
                for (Task task : productLineTasks) {
                    if (task.getStatus() == Status.taskStatus.PENDING) {

                        new Thread(() -> {
                            try {
                                executeTask(task);
                            } catch (Exception e) {
                                FileManager.logError(
                                        "Line " + lineId +
                                                " | Task " + task.taskID +
                                                " | " + e.getMessage());
                                task.cancel();
                            }
                        }).start();
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                FileManager.logError(
                        "Line :" + lineId + " interrupted :" + e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

        if (state == State.STOP) {
            throw new IllegalStateException(
                    "Line " + lineId + " is STOPPED.");
        }

        if (state == State.MAINTENANCE) {
            throw new IllegalStateException(
                    "Line " + lineId + " is under MAINTENANCE.");
        }
    }
}
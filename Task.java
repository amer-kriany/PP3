import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Task {
    private ProductLine productLine;
    private String TaskName;
    private String clientName;
    private Product product; 
    private int quantity;
    static int counter = 0;
    int taskID;
    LocalDateTime startAppointment;
    LocalDateTime deadLine = null;

    Status.taskStatus State;
    private int productionProgress = 0;

    // Constructors
    Task() {}

    Task(String TaskName, String clientName, Product product, int quantity, String endDate) {
        this.product = product;
        this.clientName = clientName;
        this.quantity = quantity;
        this.TaskName = TaskName;
        taskID = ++counter;
        this.State = Status.taskStatus.PENDING;

        startAppointment = LocalDateTime.now();
        setDeadLine(endDate);
    }

    // deadLine
    public void setDeadLine(String endLine) {
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try {
            LocalDateTime parsed = LocalDateTime.parse(endLine, formatterEnd);
            if (parsed.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Deadline cannot be before today");
            }
            this.deadLine = parsed;
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use dd-MM-yyyy HH:mm:ss");
        }
    }

    // setter
    public void updateProductionProgress(int progress) {
        this.productionProgress += progress;
    }

    // getters
    public String getClientName() { return this.clientName; }
    public Product getProduct() { return this.product; }
    public int getQuantity() { return this.quantity; }
    public Status.taskStatus getStatus() { return this.State; }
    public int getProductionProgress() { return productionProgress; }
    public int getTaskID() { return taskID; }
    public LocalDateTime getStartAppointment() { return startAppointment; }
    public LocalDateTime getDeadLine() { return deadLine; }
    public String getTaskName() { return TaskName; }

    public void setProductLine(ProductLine productLine) {
        if (productLine == null) {
            throw new IllegalArgumentException("ProductLine cannot be null");
        }
        this.productLine = productLine;
    }

    // Task lifecycle methods
    public void start() {
        if (State != Status.taskStatus.PENDING) {
            throw new IllegalStateException("Task can only be started from PENDING state");
        }
        productionProgress = 0;
        State = Status.taskStatus.IN_PROGRESS;
    }

    public void complete() {
        if (State != Status.taskStatus.IN_PROGRESS) {
            throw new IllegalStateException("Task must be IN_PROGRESS to complete");
        }
        State = Status.taskStatus.COMPLETED;
        productionProgress = this.quantity;

        // بعد إكمال التصنيع: إضافة المنتج النهائي للمخزون
        Inventory.addFinishedProduct(this.product, this.quantity);
    }

    public void cancel() {
        if (State == Status.taskStatus.COMPLETED) {
            throw new IllegalStateException("Completed task cannot be canceled");
        }
        State = Status.taskStatus.CANCELED;
        productionProgress = 0;
        productLine.cancelTask(this);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + getTaskID() +
                ", client=" + getClientName() +
                ", product=" + product.getProName() +
                ", quantity=" + getQuantity() +
                ", status=" + getStatus() +
                ", startDate=" + getStartAppointment() +
                ", deadLine=" + getDeadLine() +
                "}";
    }
}

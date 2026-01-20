import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Task {
    private ProductLine productLine;
    private String clientName;
    private String desireProduct;
    private int quantity;
    static int counter=0;
    int taskID;
    LocalDateTime startAppointment;
    LocalDateTime deadLine =null;
    
    
    Status.taskStatus State;
    // progressing percentage
    private int productionProgressPercentege = 0;
    
    
    Task(){}
    Task(String clientName,String desireProduct, int quantity , String endDate){
        
        this.desireProduct=desireProduct;
        this.clientName=clientName;
        this.quantity=quantity;
        taskID=++counter;
        this.State= Status.taskStatus.PENDING;
        
        //========================================= start/end date/time
       startAppointment=LocalDateTime.now();   
      
         setDeadLine(endDate); 
         
        }
    // deadLine
    public void setDeadLine(String endLine){
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
             try {
           LocalDateTime parsed = LocalDateTime.parse(endLine, formatterEnd);
            if(parsed.isBefore(LocalDateTime.now())){
                throw new IllegalArgumentException("Deadline cannot be before today");
            }
            this.deadLine=parsed;
        } 
        catch (DateTimeParseException e) {
            throw new IllegalArgumentException(" Invalid date format. Please use dd-MM-yyyy HH:mm:ss");
            
        }
    }
        // setter
        public void updateProductionProgressPercentege(int progress){
            this.productionProgressPercentege=progress;
            if(productionProgressPercentege>100)productionProgressPercentege=100;
            
        }
    // getters
    public String getClientName(){
        return this.clientName;
    }
    public String getDesireProduct(){
        return this.desireProduct;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public Status.taskStatus getStatus(){
        return this.State;
    }
    public int getProductionProgressPercentege(){
        return productionProgressPercentege;
    }
    public int getTaskID(){
        return taskID;
    }
    public LocalDateTime getStartAppointment() {
        return startAppointment;
    }

    public LocalDateTime getDeadLine() {
        return deadLine;
    }
    
    //============================================== Task lifecycle methods
    public void start() {
    if (State != Status.taskStatus.PENDING) {
        throw new IllegalStateException(
                "Task can only be started from PENDING state"
        );
    }
    productionProgressPercentege = 0;
    State = Status.taskStatus.IN_PROGRESS;
}

public void complete() {
    if (State != Status.taskStatus.IN_PROGRESS) {
        throw new IllegalStateException(
                "Task must be IN_PROGRESS to complete"
        );
    }
    State = Status.taskStatus.COMPLETED;
    productionProgressPercentege = 100;
}

public void cancel() {
    if (State == Status.taskStatus.COMPLETED) {
        throw new IllegalStateException(
                "Completed task cannot be canceled"
        );
    }
    State = Status.taskStatus.CANCELED;
    productionProgressPercentege = 0;
    productLine.cancelTask(this);
}

  @Override
public String toString() {
    return "Task{" +
           "id=" + getTaskID() +
           ", client= " + getClientName()  +
           ", product=" + getDesireProduct()  +
           ", quantity=" + getQuantity() +
           ", status=" + getStatus() +
           ", startDate=" + getStartAppointment() +
           ", deadLine=" + getDeadLine() +
           "}";
}   
}


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
    
     enum TaskStatus {
       PENDING,
       IN_PROGRESS,
       COMPLETED,
       CANCELED
}
TaskStatus Status;
    // progressing percentage
    double progressPercentage;
    
    Task(){}
    Task(String clientName,String desireProduct, int quantity , String endDate){
        
        this.desireProduct=desireProduct;
        this.clientName=clientName;
        this.quantity=quantity;
        taskID=++counter;
        this.Status= TaskStatus.PENDING;
        
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
    }// getters
    public String getClientName(){
        return this.clientName;
    }
    public String getDesireProduct(){
        return this.desireProduct;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public TaskStatus getStatus(){
        return this.Status;
    }   
    
    //============================================== Task lifecycle methods
    public void start() {
    if (Status != TaskStatus.PENDING) {
        throw new IllegalStateException(
                "Task can only be started from PENDING state"
        );
    }
    progressPercentage = 0;
    Status = TaskStatus.IN_PROGRESS;
}

public void complete() {
    if (Status != TaskStatus.IN_PROGRESS) {
        throw new IllegalStateException(
                "Task must be IN_PROGRESS to complete"
        );
    }
    Status = TaskStatus.COMPLETED;
    progressPercentage = 100;
}

public void cancel() {
    if (Status == TaskStatus.COMPLETED) {
        throw new IllegalStateException(
                "Completed task cannot be canceled"
        );
    }
    Status = TaskStatus.CANCELED;
}

  @Override
public String toString() {
    return "Task{" +
           "id=" + taskID +
           ", client= " + clientName  +
           ", product=" + desireProduct  +
           ", quantity=" + quantity +
           ", status=" + Status +
           ", startDate=" + startAppointment +
           ", deadLine=" + deadLine +
           "}";
}   
}


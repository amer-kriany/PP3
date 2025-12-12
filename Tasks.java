import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class Tasks {
    String client;
    String desireProduct;
    int quantity;
    int taskNumber;
    String startOppointment;
    LocalDateTime deadLine =null;
    String situation;
    
    // progressing percentege
    Tasks(){}
    Tasks(String client,String desireProduct, int quantity){
        
        this.desireProduct=desireProduct;
        this.client=client;
        this.quantity=quantity;
        
        //========================================= start date/tiime
        LocalDateTime start=LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        startOppointment = start.format(formatter);
        
       
        

    }
    public void deadLine(String endLine){
        DateTimeFormatter formatterEnd = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
             try {
            deadLine = LocalDateTime.parse(endLine, formatterEnd);
            if(deadLine.isBefore(LocalDateTime.now())){
                throw new IllegalArgumentException("DeadLine should't be before today");
            }
        } catch (DateTimeParseException e) {
            System.out.println("date/time format is wrong! please try again");
            
        }
        
       
    // if item is empty of something send an alert of choices(delay the task  until the item is here , stop the task or reject it)
    //when starting the task all the item that the task desired is taken store it in DSA
    //when starting the task the item is decreasing or when it finished as you want
    //when the task is finished the product will be added to the product clasa as a new product or if it was an old product just incread the quantity
    

    
        
        
    }
}


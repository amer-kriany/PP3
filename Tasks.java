import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class Tasks {
    String client;
    String desireProduct;
    int quantity;
    int taskNumber;
    String startDate;
    LocalDateTime deadLine ;
    String situation;
    // desire productLine
    // progressing percentege
    Tasks(String client,String desireProduct, int quantity, String endDate){
        
        this.desireProduct=desireProduct;
        this.client=client;
        this.quantity=quantity;
        
        //=========================================
        LocalDateTime currenDateTime = LocalDateTime.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        startDate= currenDateTime.format(formatter);
        //=========================================
        deadLine(endDate);
        

    }
    public void deadLine(String endLine){
        DateTimeFormatter formatterEnd=DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        try{
            deadLine=LocalDateTime.parse(endLine,formatterEnd);
        }catch(DateTimeParseException e){
            System.out.println("date/time format is wrong!");
        }
    }
    // have from the item class
    // if item is empty of something send an alert of choices(delay the task  until the item is here , stop the task or reject it)
    //when starting the task all the item that the task desired is taken store it in DSA
    //when starting the task the item is decreasing or when it finished as you want
    //when the task is finished the product will be added to the product clasa as a new product or if it was an old product just incread the quantity
    

    
        
        
    }


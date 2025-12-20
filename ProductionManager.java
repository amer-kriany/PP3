import java.util.*;

public class ProductionManager {
    private ArrayList<ProductLine> productLines;

    public ProductionManager(ArrayList<ProductLine> producLines) {
        this.productLines = producLines;
    }

    public void addTask(Task task, String lineName) {
        ProductLine taskLine = chooseLine(lineName);
        if (taskLine == null)
            throw new IllegalArgumentException("Product line not found");
        taskLine.addTask(task);

    }

    public ArrayList<ProductLine> getProductLines() {
        return productLines;
    }

    public void addLine(ProductLine newLine) {
        for (ProductLine line : productLines) {

            // منع تكرار الـ ID
            if (line.getLineId() == newLine.getLineId()) {
                throw new IllegalArgumentException(
                        "Line ID already exists!");
            }

            // منع تكرار الاسم (غير حساس لحالة الأحرف)
            if (line.getLineName().equalsIgnoreCase(
                    newLine.getLineName())) {
                throw new IllegalArgumentException(
                        "Line name already exists!");
            }
        }

        productLines.add(newLine);
    }

    public void showLinesForSelectedTasksStrict(String productName, List<Integer> taskIds) {

        System.out.println("Searching tasks for product: " + productName + " | Task IDs: " + taskIds);
        for (ProductLine line : productLines) {
            boolean linePrinted = false;

            for (Integer id : taskIds) {
                boolean found = false;

                for (Task task : line.getProductLineTasks()) {
                    if (!task.getDesireProduct().equalsIgnoreCase(productName))throw new IllegalArgumentException("There is no product by that name.");
                    if (task.getDesireProduct().equalsIgnoreCase(productName)
                            && task.taskID == id) {
                              if (!linePrinted) {
                            System.out.println("Line ID: " + line.getLineId() +
                                    " | Name: " + line.getLineName());
                            linePrinted = true;
                        }

                        System.out.println("   Task: " + task);
                        found = true;
                        break;           }
                }

                if (!found) 
                     throw new IllegalArgumentException("This task does not exist.");
                
            }
        }
    }

    public ProductLine chooseLine(String lineName) {
        for (ProductLine line : productLines) {
            if (lineName.equals(line.getLineName())) {
                return line;
            }
        }
        return null;
    }

}
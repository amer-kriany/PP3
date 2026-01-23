import java.time.LocalDateTime;
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
        task.setProductLine(taskLine);
        taskLine.addTask(task);

    }

    public ArrayList<ProductLine> getProductLines() {
        return productLines;
    }

    public void addLine(ProductLine newLine) {
        for (ProductLine line : productLines) {

            if (line.getLineId() == newLine.getLineId()) {
                throw new IllegalArgumentException(
                        "Line ID already exists!");
            }

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
                    if (!task.getDesireProduct().equalsIgnoreCase(productName))
                        throw new IllegalArgumentException("There is no product by that name.");
                    if (task.getDesireProduct().equalsIgnoreCase(productName)
                            && task.taskID == id) {
                        if (!linePrinted) {
                            System.out.println("Line ID: " + line.getLineId() +
                                    " | Name: " + line.getLineName());
                            linePrinted = true;
                        }

                        System.out.println("   Task: " + task);
                        found = true;
                    }
                }

                if (!found)
                    throw new IllegalArgumentException("This task does not exist.");

            }
        }
    }

    public void showProductsByLine(String lineName) {

        ProductLine line = chooseLine(lineName);

        if (line == null) {
            throw new IllegalArgumentException("Product line not found");
        }

        System.out.println("Products manufactured by line: " + line.getLineName());

        List<String> printedProducts = new ArrayList<>();

        for (Task task : line.getProductLineTasks()) {
            String productName = task.getDesireProduct();

            if (!printedProducts.contains(productName)) {
                System.out.println("Product: " + productName);
                printedProducts.add(productName);
            }
        }

        if (printedProducts.isEmpty())
            throw new IllegalArgumentException("There are no products manufactured using this line.");
    }

    public void showAllManufacturedProducts() {

        List<String> printedProducts = new ArrayList<>();
        boolean foundAny = false;

        System.out.println("Products manufactured by all production lines:");

        for (ProductLine line : productLines) {
            for (Task task : line.getProductLineTasks()) {

                if (task.getStatus() == Status.taskStatus.COMPLETED) {

                    String productName = task.getDesireProduct();

                    if (!printedProducts.contains(productName)) {
                        System.out.println("Product: " + productName);
                        printedProducts.add(productName);
                        foundAny = true;
                    }
                }
            }
        }

        if (!foundAny) {
            throw new IllegalArgumentException(
                    "No manufactured products found in any production line.");
        }
    }

    public String getMostRequestedProduct(
            LocalDateTime from,
            LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
        Map<String, Integer> productCount = new HashMap<>();

        for (ProductLine line : productLines) {
            synchronized (line.getProductLineTasks()) {
                for (Task task : line.getProductLineTasks()) {

                    if (task.getStatus() != Status.taskStatus.COMPLETED) {
                        continue;
                    }

                    LocalDateTime taskDate = task.getStartAppointment();

                    if (taskDate.isBefore(from) || taskDate.isAfter(to)) {
                        continue;
                    }

                    String product = task.getDesireProduct();
                    int quantity = task.getQuantity();

                    productCount.put(
                            product,
                            productCount.getOrDefault(product, 0) + quantity);
                }
            }
        }

        String mostRequestedProduct = null;
        int maxQty = 0;
        for (Map.Entry<String, Integer> entry : productCount.entrySet()) {
            if (entry.getValue() > maxQty) {
                maxQty = entry.getValue();
                mostRequestedProduct = entry.getKey();
            }
        }

        if (mostRequestedProduct != null) {
            return mostRequestedProduct + " (Total ordered: " + maxQty + ")";
        } else {
            return "No orders in this period";
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
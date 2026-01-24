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
        taskLine.addTask(task);
    }

    public ArrayList<ProductLine> getProductLines() {
        return productLines;
    }

    public void addLine(ProductLine newLine) {
        for (ProductLine line : productLines) {
            if (line.getLineId() == newLine.getLineId()) {
                throw new IllegalArgumentException("Line ID already exists!");
            }
            if (line.getLineName().equalsIgnoreCase(newLine.getLineName())) {
                throw new IllegalArgumentException("Line name already exists!");
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
                    String taskProductName = task.getProduct().getProName();
                    if (!taskProductName.equalsIgnoreCase(productName))
                        throw new IllegalArgumentException("There is no product by that name.");
                    if (taskProductName.equalsIgnoreCase(productName) && task.getTaskID() == id) {
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

    public List<String> showProductsByLine(String lineName) {
        ProductLine line = chooseLine(lineName);

        if (line == null) {
            throw new IllegalArgumentException("Product line not found");
        }

        List<String> printedProducts = new ArrayList<>();

        for (Task task : line.getProductLineTasks()) {
            String productName = task.getProduct().getProName();

            if (!printedProducts.contains(productName)) {
                printedProducts.add(productName);
            }
        }

        if (printedProducts.isEmpty())
            throw new IllegalArgumentException("There are no products manufactured using this line.");
        return printedProducts;
    }

    public ProductSale getMostRequestedProduct(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("From date must be before To date");
        }
        Map<String, Integer> productCount = new HashMap<>();

        for (ProductLine line : productLines) {
            synchronized (line.getProductLineTasks()) {
                for (Task task : line.getProductLineTasks()) {
                    if (task.getStatus() != Status.taskStatus.COMPLETED) continue;

                    LocalDateTime taskDate = task.getStartAppointment();
                    if (taskDate.isBefore(from) || taskDate.isAfter(to)) continue;

                    String product = task.getProduct().getProName();
                    int quantity = task.getQuantity();

                    productCount.put(product, productCount.getOrDefault(product, 0) + quantity);
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
            return new ProductSale(mostRequestedProduct, maxQty);
        } else {
            throw new IllegalArgumentException("No products found in the given date range.");
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

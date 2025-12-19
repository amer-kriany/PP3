import java.util.ArrayList;

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

    public ProductLine chooseLine(String lineName) {
        for (ProductLine line : productLines) {
            if (lineName.equals(line.getLineName())) {
                return line;
            }
        }
        return null;
    }

}